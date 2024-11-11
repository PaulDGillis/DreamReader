package com.pgillis.dream.core.file.parser.di

import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.parser.KsoupParser
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class EpubParserModule {
    @Single(binds = [EpubParser::class])
    fun providesEpubParser(): EpubParser = KsoupParser()
}