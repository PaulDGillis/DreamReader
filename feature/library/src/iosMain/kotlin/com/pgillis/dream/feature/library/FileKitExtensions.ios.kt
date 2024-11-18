package com.pgillis.dream.feature.library

import io.github.vinceglb.filekit.core.PlatformDirectory
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSURL

internal actual fun PlatformDirectory.toPath(): Path = this.nsUrl.path.toPath()