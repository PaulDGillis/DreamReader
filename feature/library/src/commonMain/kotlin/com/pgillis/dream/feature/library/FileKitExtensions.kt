package com.pgillis.dream.feature.library

import io.github.vinceglb.filekit.core.PlatformDirectory
import okio.Path

internal expect fun PlatformDirectory.toPath(): Path