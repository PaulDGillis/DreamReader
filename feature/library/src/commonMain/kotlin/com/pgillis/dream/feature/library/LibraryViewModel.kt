package com.pgillis.dream.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pgillis.dream.core.database.dao.BookDao
import com.pgillis.dream.core.database.model.BookWithManifest
import com.pgillis.dream.core.database.model.asBook
import com.pgillis.dream.core.database.model.asBookEntity
import com.pgillis.dream.core.database.model.asManifestDataEntities
import com.pgillis.dream.core.database.model.asMetaDataEntity
import com.pgillis.dream.core.datastore.SettingsStore
import com.pgillis.dream.core.file.FileManager
import com.pgillis.dream.core.file.platform.asIPlatformFile
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.Settings
import com.pgillis.dream.shared.Platform
import com.pgillis.dream.shared.IPlatform
import dev.zwander.kotlin.file.IPlatformFile
import dev.zwander.kotlin.file.filekit.toKmpFile
import io.github.vinceglb.filekit.core.PlatformDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LibraryViewModel(
    private val settingsStore: SettingsStore,
    private val bookDao: BookDao,
    private val fileManager: FileManager
): ViewModel() {
    private val settings: Flow<Settings> = settingsStore.settings
    private val books: Flow<List<Book>> = bookDao.getBooks().map { it.map(BookWithManifest::asBook) }

    val uiState: StateFlow<LibraryUIState> = settings.combine(books) { settings, books ->
        when {
            books.isNotEmpty() -> LibraryUIState.Success(books)
            settings.libraryDir == null || settings.libraryDir?.isEmpty() == true -> LibraryUIState.NeedsDirectory
            else -> LibraryUIState.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LibraryUIState.Loading)

    init {
        viewModelScope.launch {
            val libraryDir = settings.first().libraryDir?.asIPlatformFile() ?: return@launch
            updateLibrary(libraryDir)
        }
    }

    fun onDirectorySelected(directory: PlatformDirectory) {
        val libraryDirectory = if (Platform == IPlatform.Ios) {
            directory.path?.asIPlatformFile() ?: return
        } else directory.toKmpFile()
        viewModelScope.launch(Dispatchers.IO) {
            settingsStore.update { it.copy(libraryDir = libraryDirectory.getPath()) }
        }
        updateLibrary(libraryDirectory)
    }

    private fun updateLibrary(libraryDirectory: IPlatformFile) = viewModelScope.launch(Dispatchers.IO) {
        val idsToKeep = mutableSetOf<String>()
        fileManager.loadLibrary(libraryDirectory).collect { book ->
            idsToKeep.add(book.id)
            bookDao.insertBook(
                book.asBookEntity(),
                book.asMetaDataEntity(),
                book.asManifestDataEntities()
            )
        }
        bookDao.deleteOldBooks(idsToKeep)
    }
}