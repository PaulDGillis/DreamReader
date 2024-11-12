package com.pgillis.dream.feature.library.model

import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.database.model.BookWithManifest
import com.pgillis.dream.feature.library.model.asMetaData

fun BookWithManifest.asBook() = Book(
    id = book.id,
    metaData = metaData.asMetaData(),
    manifest = manifest.associate { it.key to it.value },
    spine = book.spine,
    coverUri = book.coverUri
)