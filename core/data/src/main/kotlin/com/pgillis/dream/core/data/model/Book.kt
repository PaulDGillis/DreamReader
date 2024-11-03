package com.pgillis.dream.core.data.model

import com.pgillis.dream.core.database.model.BookEntity
import com.pgillis.dream.core.database.model.ManifestEntity
import com.pgillis.dream.core.database.model.MetaDataEntity
import com.pgillis.dream.core.model.Book

fun Book.asEntity(): Triple<BookEntity, MetaDataEntity, List<ManifestEntity>> =
    Triple(
        BookEntity(id, spine, coverUri),
        metaData.asEntity(id),
        manifest.asManifestEntity(id)
    )