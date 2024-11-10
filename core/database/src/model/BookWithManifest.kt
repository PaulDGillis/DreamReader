package com.pgillis.dream.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.pgillis.dream.core.model.Book

data class BookWithManifest(
    @Embedded val book: BookEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val metaData: MetaDataEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val manifest: List<ManifestEntity>
)

fun BookWithManifest.asBook() = Book(
    id = book.id,
    metaData = metaData.asMetaData(),
    manifest = manifest.associate { it.key to it.value },
    spine = book.spine,
    coverUri = book.coverUri
)