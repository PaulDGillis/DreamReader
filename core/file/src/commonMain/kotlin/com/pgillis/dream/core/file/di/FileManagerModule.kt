package com.pgillis.dream.core.file.di

import com.pgillis.dream.core.file.FileManager
import com.pgillis.dream.core.file.parser.di.epubParserModule
import org.koin.dsl.module

val fileManagerModule = module {
    includes(epubParserModule)
    single { FileManager(get()) }
}
