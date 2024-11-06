package com.pgillis.dream.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pgillis.dream.core.database.model.BookEntity
import com.pgillis.dream.core.database.model.BookWithManifest
import com.pgillis.dream.core.database.model.ManifestEntity
import com.pgillis.dream.core.database.model.MetaDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Transaction
    @Query("SELECT * FROM BookEntity")
    fun getBooks(): Flow<List<BookWithManifest>>

    @Transaction
    fun insertBook(book: BookEntity, metadata: MetaDataEntity, manifest: List<ManifestEntity>) {
        insertBook(book)
        insertMetadata(metadata)
        insertManifest(manifest)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMetadata(metadata: MetaDataEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertManifest(manifest: List<ManifestEntity>)

    @Query("DELETE FROM BookEntity WHERE BookEntity.id NOT IN(:bookIds)")
    fun deleteOldBooks(bookIds: Set<String>)
}