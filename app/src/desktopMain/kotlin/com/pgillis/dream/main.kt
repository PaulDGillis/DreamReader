package com.pgillis.dream

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pgillis.dream.core.database.di.DatabaseModule
import com.pgillis.dream.core.datastore.di.SettingsStoreModule
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.core.file.di.FileManagerModule
import com.pgillis.dream.feature.library.LibraryScreen
import io.github.vinceglb.filekit.core.PlatformDirectory
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

fun main() {
    startKoin {
        modules(
            defaultModule,
            DatabaseModule().module,
            SettingsStoreModule().module,
            FileManagerModule().module
        )
    }
    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "DreamReader",
        ) {
            DreamReaderTheme {
                LibraryScreen { _: PlatformDirectory ->
//                    platformDirectory.requestPlatformPermission(context)
                }
            }
        }
    }
}