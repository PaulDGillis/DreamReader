package com.pgillis.dream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.feature.library.LibraryScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.vinceglb.filekit.core.FileKit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
        enableEdgeToEdge()
        setContent {
            DreamReaderTheme {
                LibraryScreen()
            }
        }
    }
}