package com.pgillis.dream.core.file.platform.di

import com.pgillis.dream.core.file.platform.CompressionManager
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class CompressionManagerModule {
    @Single(binds = [CompressionManager::class])
    fun providesCompressionManager(): CompressionManager = createCompressionManager()
}

expect fun createCompressionManager(): CompressionManager
