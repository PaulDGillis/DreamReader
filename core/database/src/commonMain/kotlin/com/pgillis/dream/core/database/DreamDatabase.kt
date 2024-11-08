package com.pgillis.dream.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pgillis.dream.core.database.dao.BookDao
import com.pgillis.dream.core.database.model.BookEntity
import com.pgillis.dream.core.database.model.ManifestEntity
import com.pgillis.dream.core.database.model.MetaDataEntity
import com.pgillis.dream.core.database.util.SpineConverter

@Database(
    entities = [BookEntity::class, MetaDataEntity::class, ManifestEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(SpineConverter::class)
abstract class DreamDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}