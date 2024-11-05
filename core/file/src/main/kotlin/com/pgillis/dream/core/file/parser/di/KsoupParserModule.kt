package com.pgillis.dream.core.file.parser.di

import com.pgillis.dream.core.file.parser.KsoupParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object KsoupParserModule {
    @Provides
    fun providesKsoupParser() = KsoupParser
}