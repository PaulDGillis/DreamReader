package com.pgillis.dream.core.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.pgillis.dream.core.datastore.SettingsStore
import okio.Path.Companion.toPath
import org.koin.dsl.module

expect fun producePath(): String

val settingsStoreModule = module {
    single {
        SettingsStore(
            PreferenceDataStoreFactory.createWithPath(
                produceFile = { producePath().toPath() }
            )
        )
    }
}

internal const val dataStoreFileName = "dream.preferences_pb"