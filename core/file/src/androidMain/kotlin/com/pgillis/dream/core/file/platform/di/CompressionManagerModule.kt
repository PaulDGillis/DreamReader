package com.pgillis.dream.core.file.platform.di

import android.content.Context
//import com.pgillis.dream.core.file.platform.AndroidCompressionManager
import com.pgillis.dream.core.file.platform.AndroidOkioCompressionManager
import com.pgillis.dream.core.file.platform.CompressionManager
import org.koin.java.KoinJavaComponent.inject

actual fun createCompressionManager(): CompressionManager {
    val context: Context by inject(Context::class.java)
    val appContext = context.applicationContext
//    return AndroidCompressionManager(appContext)
    return AndroidOkioCompressionManager(appContext)
}