package com.pgillis.dream.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookEntity(
    @PrimaryKey val id: String,
    val spine: LinkedHashSet<String>
)