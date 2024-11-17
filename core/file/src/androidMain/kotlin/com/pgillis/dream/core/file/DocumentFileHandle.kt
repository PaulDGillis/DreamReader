package com.pgillis.dream.core.file

import android.content.Context
import android.system.Os.*
import androidx.documentfile.provider.DocumentFile
import okio.FileHandle
import okio.IOException

class DocumentFileHandle(
    context: Context,
    documentFile: DocumentFile,
    readWrite: Boolean
): FileHandle(readWrite) {
    private val pfd = context.contentResolver.openFileDescriptor(
        documentFile.uri,
        if (readWrite) "rw" else "r"
    )
    private val fd = pfd?.fileDescriptor

    override fun protectedRead(fileOffset: Long, array: ByteArray, arrayOffset: Int, byteCount: Int): Int {
        val bytesRead = if (array.isNotEmpty()) {
            pread(fd, array, arrayOffset, byteCount, fileOffset)
        } else {
            0
        }
        if (bytesRead == -1) throw IOException("RIP dude")
        if (bytesRead == 0) return -1
        return bytesRead
    }

    override fun protectedWrite(fileOffset: Long, array: ByteArray, arrayOffset: Int, byteCount: Int) {
        pwrite(fd, array, arrayOffset, byteCount, fileOffset)
    }

    override fun protectedSize(): Long = fstat(fd).st_size

    override fun protectedResize(size: Long) {
        val currentSize = size()
        val delta = size - currentSize
        if (delta > 0) {
            protectedWrite(currentSize, ByteArray(delta.toInt()), 0, delta.toInt())
        } else {
            ftruncate(fd, size)
        }
    }

    override fun protectedFlush() {
        fsync(fd)
    }

    override fun protectedClose() {
        pfd?.close()
    }
}