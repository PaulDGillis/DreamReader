package com.pgillis.dream.core.file.platform

import dev.zwander.kotlin.file.FileUtils

fun String.asIPlatformFile(isDirectory: Boolean = true) =
    FileUtils.fromString(this, isDirectory)