package com.pgillis.dream.core.file.platform.di

import com.pgillis.dream.core.file.platform.FileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FileManagerModule {
    @Singleton
    @Provides
    fun providesFileManager() = FileManager()
}