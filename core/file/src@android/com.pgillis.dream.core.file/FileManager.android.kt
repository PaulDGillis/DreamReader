package com.pgillis.dream.core.file

import android.content.Context
import okio.FileSystem
import org.koin.java.KoinJavaComponent.inject

actual fun createFileSystemAt(path: String): FileSystem {
    val context by inject<Context>(Context::class.java)
    return DocumentTreeFileSystem(context, path)
}