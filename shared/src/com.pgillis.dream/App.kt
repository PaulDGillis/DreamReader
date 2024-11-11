package com.pgillis.dream

import androidx.compose.runtime.Composable
import com.pgillis.dream.core.datastore.di.SettingsStoreModule
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.core.file.di.FileManagerModule
import com.pgillis.dream.feature.library.LibraryScreen
import io.github.vinceglb.filekit.core.PlatformDirectory
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

@Composable
fun App() {
    DreamReaderTheme {
        KoinApplication(
            application = {
                modules(
                    defaultModule,
                    DatabaseModule().module,
                    SettingsStoreModule().module,
                    FileManagerModule().module
                )
            }
        ) {
            LibraryScreen { _: PlatformDirectory ->
//                    platformDirectory.requestPlatformPermission(context)
            }
        }
    }
}