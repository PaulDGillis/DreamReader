package com.pgillis.dream.feature.library.model

import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.database.model.ManifestEntity

fun Book.asManifestDataEntities() = manifest.asManifestEntity(id)

internal fun Map<String, String>.asManifestEntity(bookId: String) = map { (key, value) ->
    ManifestEntity(bookId, key, value)
}