package com.pgillis.dream.core.file.platform.di

import com.pgillis.dream.core.file.platform.CompressionManager
import com.pgillis.dream.core.file.platform.iOSCompressionManager

actual fun createCompressionManager(): CompressionManager = iOSCompressionManager()