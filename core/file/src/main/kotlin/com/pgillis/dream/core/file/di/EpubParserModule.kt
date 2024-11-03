package com.pgillis.dream.core.file.di

import android.content.Context
import com.pgillis.dream.core.file.EpubParser
import com.pgillis.dream.core.file.platform.AndroidFileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EpubParserModule {
    @Provides
    fun providesEpubParser(
        @ApplicationContext context: Context,
        fileManager: AndroidFileManager
    ) = EpubParser(context, fileManager)
}