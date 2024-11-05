package com.pgillis.dream.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import com.pgillis.dream.core.model.Book

@Entity(primaryKeys = ["bookId", "key", "value"], foreignKeys = [
    ForeignKey(
        entity = BookEntity::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )
])
data class ManifestEntity(
    val bookId: String,
    val key: String,
    val value: String
)

fun Book.asManifestDataEntities() = manifest.asManifestEntity(id)

internal fun Map<String, String>.asManifestEntity(bookId: String) = map { (key, value) ->
    ManifestEntity(bookId, key, value)
}