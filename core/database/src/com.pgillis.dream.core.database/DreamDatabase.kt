package com.pgillis.dream.core.database

import androidx.room.*
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
@TypeConverters(SpineConverter::class, builtInTypeConverters = BuiltInTypeConverters())
@ConstructedBy(AppDatabaseConstructor::class)
abstract class DreamDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<DreamDatabase> {
    override fun initialize(): DreamDatabase
}