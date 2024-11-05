package com.pgillis.dream.core.file.parser.di

import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.parser.KsoupParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class EpubParserModule {
    @Binds
    abstract fun bindsEpubParser(
        epubParser: KsoupParser
    ): EpubParser
}