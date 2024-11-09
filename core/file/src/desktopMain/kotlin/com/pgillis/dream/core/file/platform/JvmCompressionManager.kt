package com.pgillis.dream.core.file.platform

import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.openZip

class JvmCompressionManager: CompressionManager {
    override fun decompressOrFind(
        file: IPlatformFile,
        bookCacheFolder: IPlatformFile
    ): Flow<IPlatformFile?> = flow {
        val zipFileSystem = FileSystem.SYSTEM.openZip(file.getPath().toPath())
        val fileSystem = FileSystem.SYSTEM
        val paths = zipFileSystem.listRecursively("/".toPath())
            .filter { zipFileSystem.metadata(it).isRegularFile }
            .toList()

        val bookCacheFolderPath = bookCacheFolder.getPath().toPath()

        try {
            paths.forEach { zipFilePath ->
                zipFileSystem.source(zipFilePath).buffer().use { source ->
                    val relativeFilePath = zipFilePath.toString().trimStart('/')
                    val fileToWrite = bookCacheFolderPath.resolve(relativeFilePath)
                    fileToWrite.createParentDirectories()
                    fileSystem.sink(fileToWrite).buffer().use { sink ->
                        val bytes = sink.writeAll(source)
                        println("Unzipped: $relativeFilePath to $fileToWrite; $bytes bytes written")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        emit(bookCacheFolder)
    }


    fun Path.createParentDirectories() {
        this.parent?.let { parent ->
            FileSystem.SYSTEM.createDirectories(parent)
        }
    }
}