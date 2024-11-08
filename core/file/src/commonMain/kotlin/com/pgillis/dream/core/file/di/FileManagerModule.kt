package com.pgillis.dream.core.file.di

import com.pgillis.dream.core.file.FileManager
import com.pgillis.dream.core.file.parser.EpubParser
import com.pgillis.dream.core.file.parser.di.EpubParserModule
import com.pgillis.dream.core.file.platform.CompressionManager
import com.pgillis.dream.core.file.platform.di.CompressionManagerModule
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [EpubParserModule::class, CompressionManagerModule::class])
class FileManagerModule {
    @Single
    fun providesFileManager(
        compressionManager: CompressionManager,
        parser: EpubParser
    ): FileManager = FileManager(compressionManager, parser)
}