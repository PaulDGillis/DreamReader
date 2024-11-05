package com.pgillis.dream.core.data.di

import com.pgillis.dream.core.data.BookRepo
import com.pgillis.dream.core.database.dao.BookDao
import com.pgillis.dream.core.datastore.SettingsStore
import com.pgillis.dream.core.file.EpubParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object BookRepoModule {
    @Provides
    fun providesBookRepo(
        bookDao: BookDao,
        parser: EpubParser,
        settingsStore: SettingsStore
    ) = BookRepo(bookDao, parser, settingsStore)
}