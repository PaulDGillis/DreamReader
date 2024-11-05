package com.pgillis.dream.core.file

import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.tracing.trace
import com.anggrayudi.storage.file.getBasePath
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.io.SourceReader
import com.fleeksoft.ksoup.io.from
import com.fleeksoft.ksoup.parser.Parser
import com.pgillis.dream.core.file.platform.FileManager
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class EpubParser @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileManager: FileManager
) {
    fun loadLibrary(libraryDir: IPlatformFile) = trace("Dream LoadLibrary") {
        val bookFiles = fileManager.listFilesRecursively(libraryDir).filter { it.getName().contains(".epub") }
        fileManager.decompressEpubs(libraryDir).mapNotNull {
            if (it != null) {
                try {
                    parse(it)
                } catch (e: Exception) {
                    Log.e("Dream LoadLibrary", e.message ?: "Unknown error")
                    null
                }
            } else { null }
        }
    }

    private fun parse(epubCacheDirectory: DocumentFile): Book = trace("Dream parseDocFile") {
        val epubName = epubCacheDirectory.name
        // Read zip directory file paths
        val files = fileManager.listFiles(epubCacheDirectory).associateBy {
            it.getBasePath(context).split(epubCacheDirectory.getBasePath(context))[1]
        }

        // Check CONTAINER_PATH exists in epub
        val containerPath = files["/META-INF/container.xml"] //?: throw Exception("Can't find META-INF in .cache/$epubName")

        // Find root epub file in CONTAINER_PATH or attempt to manually find a .opf
        val rootFileStr = if (containerPath != null) {
            fileManager.openBufferedStream(containerPath).use { source ->
                Ksoup.parse(
                    sourceReader = SourceReader.from(source),
                    baseUri = "",
                    parser = Parser.xmlParser()
                )
            }.let {
                "/" + (it.selectFirst("rootfile")
                    ?.attribute("full-path")
                    ?.value
                    ?: throw Exception("Can't find epub's rootfile full-path"))
            }
        } else {
            files.keys.find { it.contains(".opf") }
        }

        // Check rootFileStr exists in epub
        val temp = rootFileStr?.removePrefix("/")?.split("/")?.toMutableList()
        temp?.removeAt(temp.size - 1)
        val rootFileDir = temp?.joinToString(separator = "/", prefix = "/")
        val rootFile = files[rootFileStr] ?: throw Exception("Can't find rootFile in .cache/$epubName")

        val rootFileDocument = fileManager.openBufferedStream(rootFile).use { source ->
            Ksoup.parse(sourceReader = SourceReader.from(source),
                baseUri = "", parser = Parser.xmlParser())
        }

        // Parse Metadata
        val children = rootFileDocument.selectFirst("metadata")?.children() ?: throw Exception("Can't find metadata in .cache/$epubName/$rootFileStr")

        val bookId = children.select("dc\\:identifier").first()?.firstChild()?.toString() ?: throw Exception("Can't find bookId in .cache/$epubName/$rootFileStr")
        val title = children.select("dc\\:title").first()?.firstChild()?.toString() ?: ""
        val language = children.select("dc\\:language").first()?.firstChild()?.toString() ?: ""
        val creator = children.select("dc\\:creator").first()?.firstChild()?.toString() ?: ""
        val cover = children.select("meta").firstNotNullOf { tag ->
            if (tag.hasAttr("name") && tag.attr("name") == "cover" && tag.hasAttr("content")) {
                tag.attr("content")
            } else null
        }

        val metadata = MetaData(
            title,
            language,
            creator
        )

        // Parse Manifest
        val manifestChildren = rootFileDocument.selectFirst("manifest")?.children() ?: throw Exception("Can't find manifest in .cache/$epubName/$rootFileStr")
        val manifestItems = manifestChildren.associate { it.attr("id") to it.attr("href") }

        val coverUri = manifestItems[cover]?.let { files["$rootFileDir/$it"]?.uri }

        // Parse Spine
        val spineChildren = rootFileDocument.selectFirst("spine")?.children() ?: throw Exception("Can't find spine in .cache/$epubName/$rootFileStr")
        val spineItems = spineChildren.map { element -> element.attr("idref") }
        val spine = LinkedHashSet<String>(spineItems)

        // Off to read it
        return@trace Book(
            bookId,
            metadata,
            manifestItems,
            spine,
            coverUri.toString()
        )
    }
}
