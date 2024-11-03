package com.pgillis.dream.core.file

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.getBasePath
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.io.SourceReader
import com.fleeksoft.ksoup.io.from
import com.fleeksoft.ksoup.parser.Parser
import com.pgillis.dream.core.file.platform.AndroidFileManager
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class EpubParser @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileManager: AndroidFileManager
) {
    fun loadLibrary(treeUri: String) = fileManager.decompressEpubs(treeUri).mapNotNull { if (it != null) parse(it) else null }

    fun parse(epubCacheDirectory: DocumentFile): Book? {
        // Read zip directory file paths
        val files = fileManager.listFiles(epubCacheDirectory).associateBy {
            it.getBasePath(context).split(epubCacheDirectory.getBasePath(context))[1]
        }

        // Check CONTAINER_PATH exists in epub
        val containerPath = "/META-INF/container.xml"
        if (!files.containsKey(containerPath)) return null // TODO Failed

        // Find root epub file in CONTAINER_PATH
        val rootFileStr = fileManager.openBufferedStream(files[containerPath]!!).use { source ->
            Ksoup.parse(sourceReader = SourceReader.from(source), baseUri =  "", parser = Parser.xmlParser())
        }.let {
            "/" + (it.selectFirst("rootfile")?.attribute("full-path")?.value ?: return null)
        }

        // Check rootFileStr exists in epub
        if (!files.containsKey(rootFileStr)) return null

        val rootFile = fileManager.openBufferedStream(files[rootFileStr]!!).use { source ->
            Ksoup.parse(sourceReader = SourceReader.from(source),
                baseUri = "", parser = Parser.xmlParser())
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
