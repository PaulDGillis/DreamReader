package com.pgillis.dream.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.pgillis.dream.core.database.DreamDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<DreamDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "dream-reader.db")
    return Room.databaseBuilder<DreamDatabase>(
        name = dbFile.absolutePath,
    )
}