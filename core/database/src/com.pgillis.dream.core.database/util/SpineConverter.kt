package com.pgillis.dream.core.database.util

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class SpineConverter {
    @TypeConverter
    fun spineToJson(spine: LinkedHashSet<String>) = Json.encodeToString<LinkedHashSet<String>>(value = spine)

    @TypeConverter
    fun jsonToSpine(spineStr: String) = Json.decodeFromString<LinkedHashSet<String>>(spineStr)
}