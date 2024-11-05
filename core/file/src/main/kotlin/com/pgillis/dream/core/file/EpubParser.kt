package com.pgillis.dream.core.file

import android.util.Log
import androidx.tracing.trace
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.io.SourceReader
import com.fleeksoft.ksoup.io.from
import com.fleeksoft.ksoup.parser.Parser
import com.pgillis.dream.core.file.platform.CompressionManager
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import dev.zwander.kotlin.file.IPlatformFile
import dev.zwander.kotlin.file.okio.toOkioSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class EpubParser @Inject constructor(
    private val compressionManager: CompressionManager
) {
    fun loadLibrary(libraryDir: IPlatformFile): List<Flow<Book>> = trace("Dream LoadLibrary") {
        val libraryCacheFolder = libraryDir.child(".cache", isDirectory = true) ?: return@trace emptyList()
        libraryDir.listFilesRecursively()
            .filter { it.isFile() && it.getName().contains("epub") }
            .mapNotNull { file ->
                libraryCacheFolder.child(file.getName(), isDirectory = true)?.let { bookCacheFolder ->
                    Pair(file, bookCacheFolder)
                }
            }.map { (file, bookCacheFolder) ->
                compressionManager.decompressOrFind(file, bookCacheFolder)
                    .filterNotNull()
                    .mapNotNull { bookFile ->
                        try {
                            parse(bookFile)
                        } catch (e: Exception) {
                            Log.e("Dream LoadLibrary", e.message ?: "Unknown error")
                            null
                        }
                    }
            }
    }

    private fun parse(bookCacheDirectory: IPlatformFile): Book = trace("Dream parseDocFile") {
        val epubName = bookCacheDirectory.nameWithoutExtension
        // Read zip directory file paths
        val files = bookCacheDirectory.listFilesRecursivelyAsMap().mapKeys { (key, _) -> "/$key" }
//            ?.associateBy {
//            it.getName()
//            it.getBasePath(context).split(epubCacheDirectory.getBasePath(context))[1]
//        }!!

        // Check CONTAINER_PATH exists in epub
        val containerXmlFile = files["/META-INF/container.xml"] //?: throw Exception("Can't find META-INF in .cache/$epubName")

        // Find root epub file in CONTAINER_PATH or attempt to manually find a .opf
        val rootFileStr = containerXmlFile?.openInputStream()?.toOkioSource()?.use { source ->
    //            fileManager.openBufferedStream(containerPath).use { source ->
            Ksoup.parse(
                sourceReader = SourceReader.from(source),
                baseUri = "",
                parser = Parser.xmlParser()
            )
        }?.let {
            "/" + (it.selectFirst("rootfile")
                ?.attribute("full-path")
                ?.value
                ?: throw Exception("Can't find epub's rootfile full-path"))
        }
            ?: files.keys.find { it.contains(".opf") }

        // Check rootFileStr exists in epub
        val temp = rootFileStr?.removePrefix("/")?.split("/")?.toMutableList()
        temp?.removeAt(temp.size - 1)
        val rootFileDir = temp?.joinToString(separator = "/", prefix = "/")
        val rootFile = files[rootFileStr] ?: throw Exception("Can't find rootFile in .cache/$epubName")

        val rootFileDocument = rootFile.openInputStream()?.toOkioSource()?.use { source ->
            Ksoup.parse(sourceReader = SourceReader.from(source),
                baseUri = "", parser = Parser.xmlParser())
        } ?: throw Exception("Unable to open rootFile")

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

        val coverUri = manifestItems[cover]?.let { files["$rootFileDir/$it"]?.getPath() }

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
