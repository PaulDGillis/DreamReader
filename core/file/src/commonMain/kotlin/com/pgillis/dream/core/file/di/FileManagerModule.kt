package com.pgillis.dream.core.file.di

import com.pgillis.dream.core.file.FileManager
import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.parser.di.EpubParserModule
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [EpubParserModule::class])
class FileManagerModule {
    @Single
    fun providesFileManager(
        parser: EpubParser
    ): FileManager = FileManager(parser)
}