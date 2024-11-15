package com.pgillis.dream.core.file.platform

import android.content.Context
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import okio.*
import okio.Path.Companion.toPath

data class DTFSException(override val message: String? = null): Exception()

class DocumentTreeFileSystem(
    private val context: Context,
    rootFilePathStr: String
): FileSystem() {
    private val rootDocumentFile = DocumentFile.fromTreeUri(context, rootFilePathStr.toUri())!!
    private val rootDocumentFilePath = AndroidIOPath(rootFilePathStr.toPath().toString())

    private fun AndroidIOPath.convertToRelativePath(): AndroidIOPath {
        return if (fullPathStr.contains("content")) {
            relativeToPath(rootDocumentFilePath)
        } else this.copy(isRelative = true)
    }

    private fun DocumentFile.findFile(path: AndroidIOPath, mkdirs: Boolean = false): DocumentFile {
        if (path.isEmpty) return this
        if (!path.isRelative) throw DTFSException("Can't use findFile on a non-relative path")

        var current = this
        path.children.forEach { childName ->
            if (childName.isEmpty) return@forEach
            val temp = current.findFile(childName)
            current = if (mkdirs && !temp.exists()) {
                current.createDirectory(childName.toString())
                    ?: throw DTFSException("Can't create directory with name $childName at $path")
            } else temp
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
        val dirPath = AndroidIOPath(dir.toString()).convertToRelativePath()
        rootDocumentFile.findFile(dirPath, true)
    }

    override fun createSymlink(source: Path, target: Path) {
        throw DTFSException("Not yet implemented")
    }

    override fun delete(path: Path, mustExist: Boolean) {
        val ioPath = AndroidIOPath(path.toString()).convertToRelativePath()

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
        val ioPath = AndroidIOPath(dir.toString()).convertToRelativePath()
        val docFile = rootDocumentFile.findFile(ioPath)
        if (docFile.isDirectory.not()) throw DTFSException("$dir is not a directory!")
        return docFile.listFiles().map { it.uri.toString().toPath() }
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
        val ioPath = AndroidIOPath(path.toString()).convertToRelativePath()
        try {
            val docFile = rootDocumentFile.findFile(ioPath, true)
            return FileMetadata(
                isRegularFile = docFile.isFile,
                isDirectory = docFile.isDirectory,
                size = docFile.length(),
                createdAtMillis = null,
                lastModifiedAtMillis = null,
                lastAccessedAtMillis = null
            )
        } catch (e: DTFSException) {
            e.printStackTrace()
            return null
        }
    }

    override fun openReadOnly(file: Path): FileHandle {
        val ioPath = AndroidIOPath(file.toString()).convertToRelativePath()
        return DocumentFileHandle(context, rootDocumentFile.findFile(ioPath), readWrite = false)
    }

    override fun openReadWrite(file: Path, mustCreate: Boolean, mustExist: Boolean): FileHandle {
        val ioPath = AndroidIOPath(file.toString()).convertToRelativePath()
        return DocumentFileHandle(context, rootDocumentFile.findFile(ioPath), readWrite = true)
    }

    override fun sink(file: Path, mustCreate: Boolean): Sink {
        val ioPath = AndroidIOPath(file.toString()).convertToRelativePath()
        val docFile = rootDocumentFile.findFile(ioPath, false)

        if (!docFile.exists()) throw IOException("Failed to create file $file")
        if (docFile.isDirectory) {
            throw IOException("HOW R U A DIRECTORY")
        }

        return docFile.uri.let { uri ->
            context.contentResolver.openOutputStream(uri)
        }?.sink()
        ?: throw IOException("Failed to open OutputStream for $file")
    }

    override fun source(file: Path): Source {
        val ioPath = AndroidIOPath(file.toString()).convertToRelativePath()
        val docFile = rootDocumentFile.findFile(ioPath)
        if (docFile.isFile.not())
            throw DTFSException("$file is not a file!")

        return context.contentResolver.openInputStream(docFile.uri)
            ?.source()
            ?: throw DTFSException("Unable to open ${docFile.uri} file input stream.")
    }
}