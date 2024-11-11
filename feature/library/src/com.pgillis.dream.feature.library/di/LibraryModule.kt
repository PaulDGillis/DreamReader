package com.pgillis.dream.feature.library.di

import com.pgillis.dream.feature.library.LibraryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val libraryModule = module {
    viewModelOf(::LibraryViewModel)
}