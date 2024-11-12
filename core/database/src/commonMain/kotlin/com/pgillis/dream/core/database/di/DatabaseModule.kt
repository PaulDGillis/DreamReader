package com.pgillis.dream.core.database.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pgillis.dream.core.database.DreamDatabase
import com.pgillis.dream.core.database.dao.BookDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
    @Single
    fun providesDatabase(): DreamDatabase = getDatabaseBuilder()
//        .addMigrations(MIGRATIONS)
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

    @Factory
    fun providesBookDao(database: DreamDatabase): BookDao = database.bookDao()
}

internal val dbName = "dream-reader.db"

expect fun getDatabaseBuilder(): RoomDatabase.Builder<DreamDatabase>