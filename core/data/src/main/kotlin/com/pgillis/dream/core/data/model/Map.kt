package com.pgillis.dream.core.data.model

import com.pgillis.dream.core.database.model.ManifestEntity

internal fun Map<String, String>.asManifestEntity(bookId: String) = map { (key, value) ->
    ManifestEntity(bookId, key, value)
}