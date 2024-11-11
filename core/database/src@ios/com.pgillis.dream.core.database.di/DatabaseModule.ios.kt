package com.pgillis.dream.core.database.di

import androidx.room.RoomDatabase
import com.pgillis.dream.core.database.DreamDatabase

actual fun getDatabaseBuilder(): RoomDatabase.Builder<DreamDatabase> {
    throw Exception("ios not ready yet")
//    val dbFile = File(System.getProperty("java.io.tmpdir"), "dream-reader.db")
//    return Room.databaseBuilder<DreamDatabase>(
//        name = dbFile.absolutePath,
//    )
}