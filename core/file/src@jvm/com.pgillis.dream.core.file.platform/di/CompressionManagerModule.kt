package com.pgillis.dream.core.file.platform.di

import com.pgillis.dream.core.file.platform.CompressionManager
import com.pgillis.dream.core.file.platform.JvmCompressionManager

actual fun createCompressionManager(): CompressionManager = JvmCompressionManager()