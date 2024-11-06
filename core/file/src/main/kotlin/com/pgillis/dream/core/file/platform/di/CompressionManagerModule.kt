package com.pgillis.dream.core.file.platform.di

import android.content.Context
import com.pgillis.dream.core.file.platform.AndroidCompressionManager
import com.pgillis.dream.core.file.platform.CompressionManager
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class CompressionManagerModule {
    @Single(binds = [CompressionManager::class])
    fun providesCompressionManager(context: Context): CompressionManager =
        AndroidCompressionManager(context)
}