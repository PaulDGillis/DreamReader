package com.pgillis.dream.core.file.parser.di

import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.parser.KsoupParser
import org.koin.dsl.module

val epubParserModule = module {
    single<EpubParser> { KsoupParser() }
}