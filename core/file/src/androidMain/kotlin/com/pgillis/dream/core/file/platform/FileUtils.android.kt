package com.pgillis.dream.core.file.platform

import dev.zwander.kotlin.file.FileUtils
import dev.zwander.kotlin.file.IPlatformFile

actual fun String.asIPlatformFile(): IPlatformFile? {
    return FileUtils.fromString(this, isDirectory = true)
}