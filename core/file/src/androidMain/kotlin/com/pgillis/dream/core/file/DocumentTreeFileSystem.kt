package com.pgillis.dream.core.file

import android.content.Context
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import okio.FileHandle
import okio.FileMetadata
import okio.FileSystem
import okio.IOException
import okio.Path
import okio.Path.Companion.toPath
import okio.Sink
import okio.Source
import okio.sink
import okio.source

data class DTFSException(override val message: String? = null): Exception()

class DocumentTreeFileSystem(
    private val context: Context,
    private val rootFilePathStr: String
): FileSystem() {
    private val rootDocumentFile = DocumentFile.fromTreeUri(context, rootFilePathStr.replace(":/", "://").toUri())!!
    private val rootDocumentFilePathStr by lazy {
        rootDocumentFile.uri.toString()
    }

    private fun String.convertToRelativePath(): String {
        return if (contains("content")) {
            substringAfterLast(rootDocumentFilePathStr).substringAfterLast(rootFilePathStr)
        } else this
    }

    private fun Path.convertToRelativePath(): Path {
        val contentCorrection = toString().convertToRelativePath() // .replace(":/", "://")
        val result = if (contentCorrection.firstOrNull() == '/') {
            contentCorrection.substring(1)
        } else contentCorrection
        return result.toPath()
    }

    private val Path.pathPartsAsNames
        get() = toString().let { pathName ->
            pathName.split(Path.DIRECTORY_SEPARATOR)
                .filter { it.isNotEmpty() }
        }

    private val Path.lastPart
        get() = toString().split(Path.DIRECTORY_SEPARATOR).last().toPath()

    private val DocumentFile.fileName
        get() = uri.path!!.substringAfterLast(Path.DIRECTORY_SEPARATOR)

    private fun DocumentFile.findFile(path: Path): DocumentFile {
        val pathStr = path.toString()
        if (pathStr == "/" || pathStr == "." || pathStr.isEmpty()) return this
        if (!path.isRelative) throw DTFSException("Can't use findFile on a non-relative path")

        var current = this
        path.pathPartsAsNames.forEach { pathPart ->
            if (pathPart.isEmpty()) return@forEach
            current = current.listFiles().firstOrNull {
                it.fileName == pathPart
            } ?: throw DTFSException("Can't find directory with name $pathPart at $path")
        }
        return current
    }

    override fun appendingSink(file: Path, mustExist: Boolean): Sink {
        throw DTFSException("Not yet implemented")
    }

    override fun atomicMove(source: Path, target: Path) {
        throw DTFSException("Not yet implemented")
    }

    override fun canonicalize(path: Path): Path {
        throw DTFSException("Not yet implemented")
    }

    override fun createDirectory(dir: Path, mustCreate: Boolean) {
        val dirPath = dir.convertToRelativePath()
        var current = rootDocumentFile
        dirPath.pathPartsAsNames.forEach { childName ->
            if (childName.isEmpty()) return@forEach
            current = current.listFiles().firstOrNull { it.fileName == childName }
                ?: current.createDirectory(childName)
                ?: throw DTFSException("Can't create directory with name $childName at $dirPath")
        }
    }

    override fun createSymlink(source: Path, target: Path) {
        throw DTFSException("Not yet implemented")
    }

    override fun delete(path: Path, mustExist: Boolean) {
        val ioPath = path.convertToRelativePath()

        try {
            rootDocumentFile.findFile(ioPath).delete()
        } catch (e: DTFSException) {
            if (mustExist) {
                e.printStackTrace()
                throw DTFSException("Unable to find $ioPath to delete")
            }
        }
    }

    override fun list(dir: Path): List<Path> {
        val ioPath = dir.convertToRelativePath()
        val docFile = rootDocumentFile.findFile(ioPath)
        if (docFile.isDirectory.not()) throw DTFSException("$dir is not a directory!")
        return docFile.listFiles().map {
            it.uri.toString().toPath()
        }
    }

    override fun listOrNull(dir: Path): List<Path>? {
        try {
            return list(dir)
        } catch (e: DTFSException) {
            e.printStackTrace()
            return null
        }
    }

    override fun metadataOrNull(path: Path): FileMetadata? {
        val ioPath = path.convertToRelativePath()
        try {
            val docFile = rootDocumentFile.findFile(ioPath)
            return FileMetadata(
                isRegularFile = docFile.isFile,
                isDirectory = docFile.isDirectory,
                size = docFile.length(),
                createdAtMillis = null,
                lastModifiedAtMillis = null,
                lastAccessedAtMillis = null
            )
        } catch (e: DTFSException) {
            return null
        }
    }

    override fun openReadOnly(file: Path): FileHandle {
        val ioPath = file.convertToRelativePath()
        return DocumentFileHandle(context, rootDocumentFile.findFile(ioPath), readWrite = false)
    }

    override fun openReadWrite(file: Path, mustCreate: Boolean, mustExist: Boolean): FileHandle {
        val ioPath = file.convertToRelativePath()
        return DocumentFileHandle(context, rootDocumentFile.findFile(ioPath), readWrite = true)
    }

    override fun sink(file: Path, mustCreate: Boolean): Sink {
        val ioPath = file.convertToRelativePath()
        val parentFolder = rootDocumentFile.findFile(ioPath.parent!!)
        val fileNamePath = ioPath.lastPart
        val docFile = parentFolder.listFiles().firstOrNull { it.name == fileNamePath.toString() }
            ?: parentFolder.createFile("", fileNamePath.toString())

        if (docFile?.exists() != true) throw IOException("Failed to create file $file")
        return docFile.uri.let { uri ->
            context.contentResolver.openOutputStream(uri)
        }?.sink()
        ?: throw IOException("Failed to open OutputStream for $file")
    }

    override fun source(file: Path): Source {
        val ioPath = file.convertToRelativePath()
        val docFile = rootDocumentFile.findFile(ioPath)
        if (docFile.isFile.not())
            throw DTFSException("$file is not a file!")

        return context.contentResolver.openInputStream(docFile.uri)
            ?.source()
            ?: throw DTFSException("Unable to open ${docFile.uri} file input stream.")
    }
}