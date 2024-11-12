package com.pgillis.dream.feature.library.model

import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import com.pgillis.dream.core.database.model.MetaDataEntity

fun MetaDataEntity.asMetaData() = MetaData(title, language, creator)

fun MetaData.asEntity(bookId: String) = MetaDataEntity(bookId, title, language, creator)

fun Book.asMetaDataEntity() = metaData.asEntity(id)