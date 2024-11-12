package com.pgillis.dream.core.file.platform

import dev.zwander.kotlin.file.IPlatformFile
import dev.zwander.kotlin.file.PlatformFile
import java.io.File

actual fun String.asIPlatformFile(): IPlatformFile? {
    if (this.isEmpty()) return null
    return PlatformFile(File(this))
}