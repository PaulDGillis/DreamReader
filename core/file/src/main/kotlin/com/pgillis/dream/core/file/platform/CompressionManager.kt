package com.pgillis.dream.core.file.platform

import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.flow.Flow

interface CompressionManager {
    fun decompressOrFind(file: IPlatformFile, bookCacheFolder: IPlatformFile): Flow<IPlatformFile?>
}