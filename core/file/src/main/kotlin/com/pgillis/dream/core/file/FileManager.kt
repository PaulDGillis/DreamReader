package com.pgillis.dream.core.file

import android.util.Log
import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.platform.CompressionManager
import com.pgillis.dream.core.model.Book
import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class FileManager @Inject constructor(
    private val compressionManager: CompressionManager,
    private val parser: EpubParser
) {
    fun loadLibrary(libraryDir: IPlatformFile): Flow<Book> {
        val libraryCacheFolder = libraryDir.child(".cache", isDirectory = true) ?: return emptyFlow()

        return libraryDir.listEpubFilesRecursively()
//                .filter { it.isFile() && it.getName().contains("epub") }
            .flatMapMerge { file ->
                val bookCacheFolder = libraryCacheFolder.child(file.getName(), isDirectory = true) ?: return@flatMapMerge emptyFlow()

                compressionManager.decompressOrFind(file, bookCacheFolder)
                    .filterNotNull()
                    .mapNotNull { bookFile ->
                        try {
                            parser.parse(bookFile)
                        } catch (e: Exception) {
                            Log.e("Dream LoadLibrary", e.message ?: "Unknown error")
                            null
                        }
                    }
            }

    }
}