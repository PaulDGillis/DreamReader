package com.pgillis.dream.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

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
