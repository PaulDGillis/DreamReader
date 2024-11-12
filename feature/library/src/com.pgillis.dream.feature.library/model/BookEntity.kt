package com.pgillis.dream.feature.library.model

import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.database.model.BookEntity

fun Book.asBookEntity() = BookEntity(id, spine, coverUri)