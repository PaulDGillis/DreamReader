package com.pgillis.dream.core.file.di

import com.pgillis.dream.core.file.FileManager
import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.platform.CompressionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FileManagerModule {
    @Provides
    fun providesFileManager(
        compressionManager: CompressionManager,
        parser: EpubParser
    ) = FileManager(compressionManager, parser)
}