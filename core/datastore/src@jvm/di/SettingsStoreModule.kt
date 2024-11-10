package com.pgillis.dream.core.datastore.di

import com.pgillis.dream.core.datastore.di.SettingsStoreModule.Companion.dataStoreFileName
import java.io.File

actual fun producePath(): String {
    val osName = System.getProperty("os.name").lowercase()

    val cacheFile = if (osName.contains("win")) {
        System.getenv("LOCALAPPDATA")
    } else if (osName.contains("mac")) {
        System.getProperty("user.home") + "/Library/Caches"
    } else if (osName.contains("nix") || osName.contains("nux")) {
        val cacheDirectory = System.getenv("XDG_CACHE_HOME")
        if (cacheDirectory == null || cacheDirectory.isEmpty()) {
            System.getProperty("user.home") + "/.cache"
        } else throw Exception("Can't find cache directory")
    } else throw Exception("Can't find cache directory")

    return cacheFile + File.pathSeparator + dataStoreFileName
}