package com.pgillis.dream

import android.content.Context
import android.content.Intent
import io.github.vinceglb.filekit.core.PlatformDirectory

internal fun PlatformDirectory.requestPlatformPermission(context: Context) {
    val contentResolver = context.contentResolver

    val takeFlags: Int =
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

    contentResolver.takePersistableUriPermission(this.uri, takeFlags)
}