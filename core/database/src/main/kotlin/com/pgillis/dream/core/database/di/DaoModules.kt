package com.pgillis.dream.core.database.di

import com.pgillis.dream.core.database.DreamDatabase
import com.pgillis.dream.core.database.dao.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModules {
    @Provides
    fun providesBookDao(database: DreamDatabase): BookDao = database.bookDao()
}