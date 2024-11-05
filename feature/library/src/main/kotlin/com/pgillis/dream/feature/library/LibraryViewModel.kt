package com.pgillis.dream.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgillis.dream.core.data.BookRepo
import com.pgillis.dream.core.datastore.SettingsStore
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.zwander.kotlin.file.FileUtils
import dev.zwander.kotlin.file.filekit.toKmpFile
import io.github.vinceglb.filekit.core.PlatformDirectory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    settingsStore: SettingsStore,
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
            val libraryDirStr = settings.first().libraryDir ?: return@launch
            val libraryDir = FileUtils.fromString(libraryDirStr, true) ?: return@launch
            bookRepo.insertOrSkipBookDir(libraryDir)
        }
    }

    fun onDirectorySelected(directory: PlatformDirectory) = viewModelScope.launch {
        bookRepo.insertOrSkipBookDir(directory.toKmpFile())
    }
}