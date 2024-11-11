package com.pgillis.dream.core.file.platform.di

import com.pgillis.dream.core.file.platform.CompressionManager
import org.koin.dsl.module

val compressionManagerModule = module {
    single<CompressionManager> { createCompressionManager() }
}

expect fun createCompressionManager(): CompressionManager
