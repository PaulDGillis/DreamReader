package com.pgillis.dream

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.core.file.FileUtils
import com.pgillis.dream.feature.library.LibraryScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformDirectory

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
        enableEdgeToEdge()
        setContent {
            DreamReaderTheme {
                LibraryScreen(onDirectorySelected = ::onDirectorySelected)
            }
        }
    }

    private fun onDirectorySelected(platformDirectory: PlatformDirectory): String? {
        val contentResolver = applicationContext.contentResolver

        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        // Check for the freshest data.
        contentResolver.takePersistableUriPermission(platformDirectory.uri, takeFlags)

        return FileUtils.getPath(this, platformDirectory.uri)
    }
}