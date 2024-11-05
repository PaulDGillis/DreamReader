package com.pgillis.dream.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pgillis.dream.core.model.Book

@Entity
data class BookEntity(
    @PrimaryKey val id: String,
    val spine: LinkedHashSet<String>,
    val coverUri: String?
)

fun Book.asBookEntity() = BookEntity(id, spine, coverUri)
