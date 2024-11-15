package com.pgillis.dream.core.file.platform

import android.webkit.MimeTypeMap
import okio.Path

data class AndroidIOPath(
    val fullPathStr: String,
    val isRelative: Boolean = false
) {
    val isEmpty: Boolean by lazy { fullPathStr == Path.DIRECTORY_SEPARATOR || fullPathStr.isEmpty() }

    val mimeType: String by lazy {
        val extension = MimeTypeMap.getFileExtensionFromUrl(fullPathStr)
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: throw DTFSException("Unable to find mimeType for $this")
    }

    val parentPath: AndroidIOPath by lazy {
        AndroidIOPath(toString().substringBeforeLast(Path.DIRECTORY_SEPARATOR), isRelative)
    }

    val child: AndroidIOPath by lazy {
        AndroidIOPath(fullPathStr.substringAfterLast(Path.DIRECTORY_SEPARATOR))
    }

    val children: List<AndroidIOPath> by lazy {
        fullPathStr.split(Path.DIRECTORY_SEPARATOR).map { AndroidIOPath(it, isRelative) }
    }

    val containsFile: Boolean by lazy {
        fullPathStr.contains(".")
    }

    fun relativeToPath(rootDocFileStrPath: AndroidIOPath): AndroidIOPath = AndroidIOPath(
        toString().substringAfterLast(rootDocFileStrPath.fullPathStr),
        isRelative = true
    )

    override fun toString() = fullPathStr
}