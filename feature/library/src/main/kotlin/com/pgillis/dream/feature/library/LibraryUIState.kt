package com.pgillis.dream.feature.library

import com.pgillis.dream.core.model.Book

sealed class LibraryUIState {
    object Loading: LibraryUIState()
    object NeedsDirectory: LibraryUIState()
    class Success(val books: List<Book>): LibraryUIState()
}