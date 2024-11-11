package com.pgillis.dream

import androidx.compose.runtime.Composable
import com.pgillis.dream.core.database.di.databaseModule
import com.pgillis.dream.core.datastore.di.settingsStoreModule
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.core.file.di.fileManagerModule
import com.pgillis.dream.feature.library.LibraryScreen
import com.pgillis.dream.feature.library.di.libraryModule
import io.github.vinceglb.filekit.core.PlatformDirectory
import org.koin.compose.KoinApplication

@Composable
fun App() {
    DreamReaderTheme {
        KoinApplication(
            application = {
                modules(
                    libraryModule,
                    databaseModule,
                    settingsStoreModule,
                    fileManagerModule
                )
            }
        ) {
            LibraryScreen { _: PlatformDirectory ->
//                    platformDirectory.requestPlatformPermission(context)
            }
        }
    }
}