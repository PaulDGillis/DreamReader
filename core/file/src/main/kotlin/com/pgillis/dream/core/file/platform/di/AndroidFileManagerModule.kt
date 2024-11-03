package com.pgillis.dream.core.file.platform.di

import android.content.Context
import com.pgillis.dream.core.file.platform.AndroidFileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AndroidFileManagerModule {
    @Singleton
    @Provides
    fun providesFileManager(@ApplicationContext context: Context) = AndroidFileManager(context)
}