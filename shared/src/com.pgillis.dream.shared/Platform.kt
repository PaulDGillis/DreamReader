package com.pgillis.dream.shared

sealed interface IPlatform {
    data object Android: IPlatform
    data object Ios: IPlatform
    data object Desktop: IPlatform
}

expect val Platform: IPlatform