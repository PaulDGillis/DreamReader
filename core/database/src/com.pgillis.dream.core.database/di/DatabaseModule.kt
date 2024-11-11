package com.pgillis.dream.core.database.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pgillis.dream.core.database.DreamDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val databaseModule = module {
    single {
        getDatabaseBuilder()
    //        .addMigrations(MIGRATIONS)
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    factory { get<DreamDatabase>().bookDao() }
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<DreamDatabase>