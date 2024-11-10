package com.pgillis.dream.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData

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

fun MetaDataEntity.asMetaData() = MetaData(title, language, creator)

fun MetaData.asEntity(bookId: String) = MetaDataEntity(bookId, title, language, creator)

fun Book.asMetaDataEntity() = metaData.asEntity(id)