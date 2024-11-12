package com.pgillis.dream.core.file.platform

import dev.zwander.kotlin.file.IPlatformFile

expect fun String.asIPlatformFile(): IPlatformFile?