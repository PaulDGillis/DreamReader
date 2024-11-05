package com.pgillis.dream.core.file.platform.di

import com.pgillis.dream.core.file.platform.AndroidCompressionManager
import com.pgillis.dream.core.file.platform.CompressionManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompressionManagerModule {
    @Binds
    abstract fun bindsCompressionManager(
        androidCompressionManager: AndroidCompressionManager
    ): CompressionManager
}