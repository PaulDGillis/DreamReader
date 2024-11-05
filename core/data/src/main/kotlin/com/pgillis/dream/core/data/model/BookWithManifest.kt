package com.pgillis.dream.core.data.model

import com.pgillis.dream.core.database.model.BookWithManifest
import com.pgillis.dream.core.model.Book

fun BookWithManifest.asBook() = Book(
    id = book.id,
    metaData = metaData.asMetaData(),
    manifest = manifest.associate { it.key to it.value },
    spine = book.spine,
    coverUri = book.coverUri
)