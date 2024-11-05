package com.pgillis.dream.core.file.di

import com.pgillis.dream.core.file.EpubParser
import com.pgillis.dream.core.file.platform.CompressionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EpubParserModule {
    @Provides
    fun providesEpubParser(
        compressionManager: CompressionManager
    ) = EpubParser(compressionManager)
}