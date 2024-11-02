package com.pgillis.dream.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgillis.dream.core.data.BookRepo
import com.pgillis.dream.core.datastore.SettingsStore
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val settingsStore: SettingsStore,
    private val bookRepo: BookRepo
): ViewModel() {
    private val settings: Flow<Settings> = settingsStore.settings
    private val books: Flow<List<Book>> = bookRepo.getBooks()

    val uiState: StateFlow<LibraryUIState> = settings.combine(books) { settings, books ->
        when {
            settings.libraryDir == null -> LibraryUIState.NeedsDirectory
            books.isNotEmpty() -> LibraryUIState.Success(books)
            else -> LibraryUIState.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LibraryUIState.Loading)


    init {
        viewModelScope.launch {
            val settings = settings.first()
            val dir = settings.libraryDir
            if (dir != null) {
                onDirectorySelected(dir)
            }
        }
    }

    fun onDirectorySelected(directory: String) {
        viewModelScope.launch {
            settingsStore.update(libraryDir = directory)
            bookRepo.insertOrSkipBookDir(directory.toPath())
        }
    }
}