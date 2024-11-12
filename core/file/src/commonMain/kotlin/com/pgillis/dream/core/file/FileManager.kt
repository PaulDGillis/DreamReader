package com.pgillis.dream.core.file

import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.platform.CompressionManager
import com.pgillis.dream.core.model.Book
import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Single

@OptIn(ExperimentalCoroutinesApi::class)
@Single
class FileManager(
    private val compressionManager: CompressionManager,
    private val parser: EpubParser
) {
    fun loadLibrary(libraryDir: IPlatformFile): Flow<Book> {
        val libraryCacheFolder = libraryDir.child(".cache", isDirectory = true) ?: return emptyFlow()
        if (!libraryCacheFolder.getExists()) {
            libraryCacheFolder.mkdir()
        }

        return libraryDir.listEpubFilesRecursively()
            .flatMapMerge { file ->
                val bookCacheFolder = libraryCacheFolder.child(file.nameWithoutExtension, isDirectory = true) ?: return@flatMapMerge emptyFlow()

                compressionManager.decompressOrFind(file, bookCacheFolder)
                    .filterNotNull()
                    .mapNotNull { bookFile ->
                        try {
                            parser.parse(bookFile)
                        } catch (e: Exception) {
                            // TODO log
                            e.printStackTrace()
                            null
                        }
                    }
            }

    }
}