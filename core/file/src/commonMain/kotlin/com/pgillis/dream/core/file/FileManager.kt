package com.pgillis.dream.core.file

import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.model.Book
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.*
import okio.Path.Companion.toPath
import org.koin.core.annotation.Single

expect fun createFileSystemAt(path: String): FileSystem

@Single
class FileManager(
    private val parser: EpubParser
) {
    private val Path.nameWithoutExtension
        get() = name.substringBeforeLast(".")
    fun loadLibrary(libraryDir: String): Flow<Book> {
        val libraryDirPath = libraryDir.toPath()
        val libraryCacheFolder = libraryDirPath / ".cache".toPath()
        val fs = createFileSystemAt(libraryDir)
        if (!fs.exists(libraryCacheFolder)) {
            fs.createDirectory(libraryCacheFolder)
        }

        return channelFlow {
            fs.listRecursively(libraryDirPath)
                .forEach { file ->
                    launch {
                        val bookCacheFolder = libraryCacheFolder / file.nameWithoutExtension
                        decompressOrFind(file, bookCacheFolder)
                            .mapNotNull { bookFile ->
                                try {
                                    parser.parse(bookFile)
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
        file: Path,
        bookCacheFolder: Path
    ): Flow<Path> {
        val fileSystem = createFileSystemAt(file.toString()) //DocumentTreeFileSystem(context, file.getPath())
        val bookCacheFS = createFileSystemAt(bookCacheFolder.toString()) // DocumentTreeFileSystem(context, bookCacheFolder.getPath())

        val zipFileSystem: FileSystem?
        try {
            zipFileSystem = fileSystem.openZip(file)
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

                            bookCacheFS.createParentDirectories(fileToWrite)
                            bookCacheFS.sink(fileToWrite).buffer().use { sink ->
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