package com.pgillis.dream.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pgillis.dream.core.datastore.SettingsStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsStoreModule {
    private val Context.dataStore by preferencesDataStore(name = "dream-reader")

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context) = context.dataStore

    @Provides
    fun providesSettingsStore(dataStore: DataStore<Preferences>) = SettingsStore(dataStore)
}