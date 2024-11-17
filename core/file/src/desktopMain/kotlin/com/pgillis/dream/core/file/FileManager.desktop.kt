package com.pgillis.dream.core.file

import okio.FileSystem

actual fun createFileSystemAt(path: String): FileSystem = FileSystem.SYSTEM