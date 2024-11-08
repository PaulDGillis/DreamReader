package com.pgillis.dream.core.datastore.di

import android.content.Context
import com.pgillis.dream.core.datastore.di.SettingsStoreModule.Companion.dataStoreFileName
import org.koin.java.KoinJavaComponent.inject

actual fun producePath(): String {
    val context: Context by inject(Context::class.java)
    return context.filesDir.resolve(dataStoreFileName).absolutePath
}