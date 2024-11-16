package com.pgillis.dream.core.file.platform

import android.content.Context
import com.pgillis.dream.shared.IPlatform
import com.pgillis.dream.shared.Platform
import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.openZip
import okio.use

class AndroidOkioCompressionManager(
    private val context: Context
): CompressionManager {
    override fun decompressOrFind(
        file: IPlatformFile,
        bookCacheFolder: IPlatformFile
    ): Flow<IPlatformFile?> = flow {
        val fileSystem = DocumentTreeFileSystem(context, file.getPath())
        val bookCacheFS = DocumentTreeFileSystem(context, bookCacheFolder.getPath())

        var zipFileSystem: FileSystem? = null
        try {
            zipFileSystem = fileSystem.openZip(file.getPath().toPath())
        } catch (e: Exception) {
            e.printStackTrace()
            return@flow
        }
        val paths = zipFileSystem.listRecursively("/".toPath())
            .filter { zipFileSystem.metadata(it).isRegularFile }
            .toList()

        val bookCacheFolderPath = bookCacheFolder.getPath().toPath()

        try {
            paths.forEach { zipFilePath ->
                zipFileSystem.source(zipFilePath).buffer().use { source ->
                    val relativeFilePath = zipFilePath.toString().trimStart('/').toPath()
//                    val fileToWrite = bookCacheFolderPath.resolveFileToWrite(relativeFilePath)

                    bookCacheFS.createParentDirectories(relativeFilePath)
                    bookCacheFS.sink(relativeFilePath).buffer().use { sink ->
                        val bytes = sink.writeAll(source)
                        println("Unzipped: $relativeFilePath; $bytes bytes written")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        emit(bookCacheFolder)
    }

    private fun Path.resolveFileToWrite(relativeFilePath: String) = if (Platform == IPlatform.Android) {
        relativeFilePath.toPath()
    } else {
        resolve(relativeFilePath)
    }

    private fun DocumentTreeFileSystem.createParentDirectories(path: Path) {
        val dirPath = path.parent
        if (dirPath != null && dirPath.toString() != ".") {
            createDirectories(dirPath)
        }
    }
}