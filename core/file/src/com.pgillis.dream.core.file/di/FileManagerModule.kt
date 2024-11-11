package com.pgillis.dream.core.file.di

import com.pgillis.dream.core.file.FileManager
import com.pgillis.dream.core.file.parser.di.epubParserModule
import com.pgillis.dream.core.file.platform.di.compressionManagerModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val fileManagerModule = module {
    includes(epubParserModule, compressionManagerModule)
    singleOf(::FileManager)
}