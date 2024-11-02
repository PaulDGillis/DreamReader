package com.pgillis.dream.core.data.di

import com.pgillis.dream.core.data.BookRepo
import com.pgillis.dream.core.database.dao.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object BookRepoModule {
    @Provides
    fun providesBookRepo(bookDao: BookDao) = BookRepo(bookDao)
}