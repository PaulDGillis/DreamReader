package com.pgillis.dream.core.file

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.io.SourceReader
import com.fleeksoft.ksoup.io.from
import com.fleeksoft.ksoup.parser.Parser
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.openZip

object EpubParser {
    fun parse(epubPath: Path): Book? {
        // Open epub as if it were a zip
        val fileSystem = FileSystem.SYSTEM
        val zipFileSystem = fileSystem.openZip(epubPath)
        // Read zip directory file paths
        val paths: Set<Path> = zipFileSystem.listRecursively("/".toPath())
            .filter { zipFileSystem.metadata(it).isRegularFile }
            .toSet()

        // Check CONTAINER_PATH exists in epub
        val containerPath = "/META-INF/container.xml".toPath()
        if (containerPath !in paths) return null // TODO Failed

        // Find root epub file in CONTAINER_PATH
        val rootFileStr = zipFileSystem.source(containerPath).buffer().use { source ->
            Ksoup.parse(sourceReader = SourceReader.from(source),
                baseUri = epubPath.toString(), parser = Parser.xmlParser())
        }.let {
            "/" + (it.selectFirst("rootfile")?.attribute("full-path")?.value ?: return null)
        }

        // Check rootFileStr exists in epub
        val rootFilePath = rootFileStr.toPath()
        if (rootFilePath !in paths) return null

        val rootFile = zipFileSystem.source(rootFilePath).buffer().use { source ->
            Ksoup.parse(sourceReader = SourceReader.from(source),
                baseUri = epubPath.toString(), parser = Parser.xmlParser())
        }

        // Parse Metadata
        val children = rootFile.selectFirst("metadata")?.children() ?: return null

        val bookId = children.select("dc\\:identifier").first()?.firstChild()?.toString() ?: ""
        val title = children.select("dc\\:title").first()?.firstChild()?.toString() ?: ""
        val language = children.select("dc\\:language").first()?.firstChild()?.toString() ?: ""
        val creator = children.select("dc\\:creator").first()?.firstChild()?.toString() ?: ""
        val cover = children.select("meta").first()?.let { tag ->
            if (tag.hasAttr("name") && tag.hasAttr("content")) {
                tag.attr("content")
            } else null
        }

        val metadata = MetaData(
            title,
            language,
            creator,
            cover
        )

        // Parse Manifest
        val manifestChildren = rootFile.selectFirst("manifest")?.children() ?: return null
        val manifestItems = manifestChildren.associate { it.attr("id") to it.attr("href") }

        // Parse Spine
        val spineChildren = rootFile.selectFirst("spine")?.children() ?: return null
        val spineItems = spineChildren.map { element -> element.attr("idref") }
        val spine = LinkedHashSet<String>(spineItems)

        // Off to read it
        return Book(
            bookId,
            metadata,
            manifestItems,
            spine
        )
    }
}
