package com.pgillis.dream.core.database.di

import android.content.Context
import androidx.room.Room
import com.pgillis.dream.core.database.DreamDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ): DreamDatabase = Room.databaseBuilder(
        context,
        DreamDatabase::class.java,
        "dream-database"
    ).build()
}