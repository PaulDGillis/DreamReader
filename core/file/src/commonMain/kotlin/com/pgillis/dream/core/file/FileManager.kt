package com.pgillis.dream.core.file

import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.openZip
import okio.use
import org.koin.core.annotation.Single

expect fun createFileSystemAt(path: String): FileSystem

@Single
class FileManager(
    private val parser: EpubParser
) {
    private val Path.nameWithoutExtension
        get() = name.substringBeforeLast(".")
    fun loadLibrary(libraryDir: Path): Flow<Book> {
        val fs = createFileSystemAt(libraryDir.toString())
        val libraryCacheFolder = libraryDir / ".cache".toPath()
        if (!fs.exists(libraryCacheFolder)) {
            fs.createDirectory(libraryCacheFolder)
        }

        return channelFlow {
            fs.listRecursively(libraryDir)
                .filter { it.name != ".cache" && it.name.contains(".epub") }
                .forEach { file ->
                    launch {
                        val bookCacheFolder = ".cache".toPath() / file.nameWithoutExtension
                        decompressOrFind(fs, file, bookCacheFolder)
                            .mapNotNull { bookFile ->
                                try {
                                    parser.parse(fs, libraryDir, bookFile)
                                } catch (e: Exception) {
                                    // TODO log
                                    e.printStackTrace()
                                    null
                                }
                            }.collect(::send)
                    }
                }
        }
    }

    private fun decompressOrFind(
        fs: FileSystem,
        file: Path,
        bookCacheFolder: Path
    ): Flow<Path> {
        val zipFileSystem: FileSystem?
        try {
            zipFileSystem = fs.openZip(file)
        } catch (e: Exception) {
            e.printStackTrace()
            return flowOf()
        }
        val paths = zipFileSystem.listRecursively("/".toPath())
            .filter { zipFileSystem.metadata(it).isRegularFile }
            .toList()

        return channelFlow {
            paths.forEach { zipFilePath ->
                launch {
                    try {
                        zipFileSystem.source(zipFilePath).buffer().use { source ->
                            val relativeFilePath = zipFilePath.toString().trimStart('/').toPath()
                            val fileToWrite = bookCacheFolder.resolve(relativeFilePath)

                            fs.createParentDirectories(fileToWrite)
                            fs.sink(fileToWrite).buffer().use { sink ->
                                val bytes = sink.writeAll(source)
                                println("Unzipped: $relativeFilePath; $bytes bytes written")
                            }
                        }
                        send(bookCacheFolder)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun FileSystem.createParentDirectories(path: Path) {
        val dirPath = path.parent
        if (dirPath != null && dirPath.toString() != ".") {
            createDirectories(dirPath)
        }
    }
}