package com.pgillis.dream.core.file.parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.io.SourceReader
import com.fleeksoft.ksoup.io.from
import com.fleeksoft.ksoup.parser.Parser
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.use

class KsoupParser: EpubParser {
    override fun parse(fs: FileSystem, bookCacheDirectory: Path): Book {
        // Find root epub file in CONTAINER_PATH or attempt to manually find a .opf
        val opfFile = bookCacheDirectory.parseMetaForOpfFile(fs)
        val opfParent = opfFile.parent!!
        // Open/Close opfFile and parse into Ksoup Document
        val opfDocument = opfFile.parseToDocument(fs)

        // Parse opfDoc into Metadata
        val metadata = MetaData(
            title = opfDocument.selectFirst("metadata > dc\\:title")?.text() ?: "",
            language = opfDocument.selectFirst("metadata > dc\\:language")?.text() ?: "",
            creator = opfDocument.selectFirst("metadata > dc\\:creator")?.text() ?: ""
        )

        val coverManifestId = opfDocument.selectFirst("metadata > meta[name='cover'][content]")
            ?.attr("content")

        var propertyCover: String? = null

        // Parse Manifest
        val manifestItems = opfDocument.select("manifest > item[id][href]")
            .associate {
                val id = it.attr("id")
                if (it.hasAttr("properties") && it.attr("properties") == coverManifestId) {
                    propertyCover = id
                }
                id to it.attr("href")
            }

        // Parse Spine
        val spineItems = opfDocument.selectFirst("spine")
            ?.children()
            ?.map { element -> element.attr("idref") }
            ?: throw Exception("Can't find spine in $bookCacheDirectory/${opfFile.name}")

        val spine = LinkedHashSet(spineItems)

        val bookId = opfDocument.selectFirst("metadata > dc\\:identifier")?.text()!!

        val coverFile = manifestItems[coverManifestId]?.let { opfRelativePath ->
            opfParent.resolve(opfRelativePath)
        } ?: manifestItems[propertyCover]?.let(opfParent::resolve)

        // Off to read it
        return Book(
            bookId,
            metadata,
            manifestItems,
            spine,
            coverFile?.toString()
        )
    }

    private fun Path.parseToDocument(fs: FileSystem) = fs.source(this).use { source ->
        Ksoup.parse(SourceReader.from(source), "", parser = Parser.xmlParser())
    }

    private fun Path.parseMetaForOpfFile(fs: FileSystem): Path {
        val metaContainer = "META-INF/container.xml".toPath()
        val fileToParse = this.resolve(metaContainer)
        val metaContainerXmlDocument = fileToParse.parseToDocument(fs)
        return metaContainerXmlDocument.selectFirst("rootfile[full-path]")
            ?.attr("full-path")
            ?.let { this / it.toPath() }
            ?: fs.listRecursively(this).first {
                it.name.contains(".opf")
            }
    }
}
