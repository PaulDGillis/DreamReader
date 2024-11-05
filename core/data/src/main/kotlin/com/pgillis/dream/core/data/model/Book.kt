package com.pgillis.dream.core.data.model

import com.pgillis.dream.core.database.model.BookEntity
import com.pgillis.dream.core.model.Book

fun Book.asBookEntity() = BookEntity(id, spine, coverUri)

fun Book.asMetaDataEntity() = metaData.asEntity(id)

fun Book.asManifestDataEntities() = manifest.asManifestEntity(id)