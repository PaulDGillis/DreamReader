package com.pgillis.dream.core.file.parser

import androidx.tracing.trace
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.io.SourceReader
import com.fleeksoft.ksoup.io.from
import com.fleeksoft.ksoup.parser.Parser
import com.pgillis.dream.core.file.findChildByPath
import com.pgillis.dream.core.file.listFilesRecursively
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import dev.zwander.kotlin.file.IPlatformFile
import dev.zwander.kotlin.file.okio.toOkioSource
import org.koin.core.annotation.Single

@Single(binds = [EpubParser::class])
class KsoupParser: EpubParser {
    override fun parse(bookCacheDirectory: IPlatformFile): Book = trace("Dream parseDocFile") {
        // Find root epub file in CONTAINER_PATH or attempt to manually find a .opf
        val opfFile = bookCacheDirectory.findOpfFile()
        val opfParent = opfFile.getParentFile()
        // Open/Close opfFile and parse into Ksoup Document
        val opfDocument = opfFile.openInputStream()?.toOkioSource()?.use { source ->
            Ksoup.parse(
                sourceReader = SourceReader.from(source),
                baseUri = "${opfParent?.getName()}",
                parser = Parser.xmlParser()
            )
        } ?: throw Exception("Unable to open rootFile")

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
            ?: throw Exception("Can't find spine in .cache/${bookCacheDirectory.nameWithoutExtension}/${opfFile.getName()}")

        val spine = LinkedHashSet<String>(spineItems)

        val bookId = opfDocument.selectFirst("metadata > dc\\:identifier")?.text()!!

        val coverFile = manifestItems[coverManifestId]?.let { opfRelativePath ->
            opfParent?.findChildByPath(opfRelativePath)
        } ?: manifestItems[propertyCover]?.let { opfRelativePath ->
            opfParent?.findChildByPath(opfRelativePath)
        }

        // Off to read it
        return@trace Book(
            bookId,
            metadata,
            manifestItems,
            spine,
            coverFile?.getPath()
        )
    }

    private fun IPlatformFile.findOpfFile() = findChildByPath("META-INF/container.xml") // Check CONTAINER_PATH exists in epub
        ?.openInputStream() // Open Input Stream if it does, try to make it an okio one
        ?.toOkioSource()
        ?.use { source -> // Open and close file and parse into Ksoup Document
            Ksoup.parse(SourceReader.from(source), "", parser = Parser.xmlParser())
        }?.let { rootXmlDocument -> // If successfully parsed try to pull the opf file path from container.xml
            rootXmlDocument.selectFirst("rootfile[full-path]")
                ?.attr("full-path")
                ?.let { findChildByPath(it) }
        } ?: listFilesRecursively() // If any null in the chain, default to manual search for .opf in bookCacheDir
                .first { it.getName().contains(".opf") }
}
