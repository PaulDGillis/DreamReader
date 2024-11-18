package com.pgillis.dream.feature.library

import okio.Path
import io.github.vinceglb.filekit.core.PlatformDirectory
import okio.Path.Companion.toPath

internal actual fun PlatformDirectory.toPath(): Path = file.path.toPath()