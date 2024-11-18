package com.pgillis.dream.shared.di

import com.pgillis.dream.feature.library.LibraryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LibraryViewModel(get(), get(), get()) }
}