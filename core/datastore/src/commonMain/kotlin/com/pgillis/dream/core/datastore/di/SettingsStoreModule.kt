package com.pgillis.dream.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.pgillis.dream.core.datastore.SettingsStore
import okio.Path.Companion.toPath
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

expect fun producePath(): String

@Module
class SettingsStoreModule {
    @Single
    fun providesSettingsStore() = SettingsStore(createDataStore())

    fun createDataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { producePath().toPath() }
        )

    companion object {
        internal const val dataStoreFileName = "dream.preferences_pb"
    }
}