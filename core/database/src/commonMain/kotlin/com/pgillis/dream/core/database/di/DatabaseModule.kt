package com.pgillis.dream.core.database.di

import android.content.Context
import androidx.room.Room
import com.pgillis.dream.core.database.DreamDatabase
import com.pgillis.dream.core.database.dao.BookDao
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
    @Single
    fun providesDatabase(
        context: Context
    ): DreamDatabase = Room.databaseBuilder(
        context,
        DreamDatabase::class.java,
        "dream-database"
    ).build()

    @Factory
    fun providesBookDao(database: DreamDatabase): BookDao = database.bookDao()
}