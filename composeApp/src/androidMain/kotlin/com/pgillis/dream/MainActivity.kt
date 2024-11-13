package com.pgillis.dream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.feature.library.LibraryScreen
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformDirectory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
        enableEdgeToEdge()
        setContent {
            DreamReaderTheme {
                val context = LocalContext.current
                LibraryScreen { platformDirectory: PlatformDirectory ->
                    platformDirectory.requestPlatformPermission(context)
                }
            }
        }
    }
}