package com.pgillis.dream.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pgillis.dream.core.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val THEME = intPreferencesKey("THEME")
        val LIBRARY_DIR = stringPreferencesKey("LIBRARY_DIR")
    }

    val settings: Flow<Settings> = dataStore.data.map { prefs ->
        Settings(
            theme = prefs[THEME]?.let { Settings.Theme.entries[it] } ?: Settings.Theme.FollowSystem,
            libraryDir = prefs[LIBRARY_DIR]
        )
    }

    suspend fun update(
        theme: Settings.Theme? = null,
        libraryDir: String? = null
    ) {
        dataStore.edit { prefs ->
            if (theme != null)
                prefs[THEME] = theme.ordinal
            if (libraryDir != null)
                prefs[LIBRARY_DIR] = libraryDir
        }
    }
}