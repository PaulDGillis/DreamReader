package com.pgillis.dream.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pgillis.dream.core.database.DreamDatabase
import org.koin.java.KoinJavaComponent.inject

actual fun getDatabaseBuilder(): RoomDatabase.Builder<DreamDatabase> {
    val context: Context by inject(Context::class.java)
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(dbName)
    return Room.databaseBuilder<DreamDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}