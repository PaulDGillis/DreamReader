package com.pgillis.dream.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MetaDataEntity(
    @PrimaryKey val bookId: String,
    val title: String,
    val language: String,
    val creator: String,
    val coverId: String? = null
)
