package com.pgillis.dream.feature.library

import android.net.Uri
import io.github.vinceglb.filekit.core.PlatformDirectory
import okio.Path
import okio.Path.Companion.toPath

internal actual fun PlatformDirectory.toPath(): Path {
    val uri: Uri = uri
    return uri.toString().toPath()
}