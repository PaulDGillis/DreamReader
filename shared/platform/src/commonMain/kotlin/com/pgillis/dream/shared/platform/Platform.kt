package com.pgillis.dream.shared.platform

sealed interface Platform {
    data object Android: Platform
    data object Ios: Platform
    data object Desktop: Platform
}

expect val PlatformType: Platform