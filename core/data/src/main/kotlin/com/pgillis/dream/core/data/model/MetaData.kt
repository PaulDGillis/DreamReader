package com.pgillis.dream.core.data.model

import com.pgillis.dream.core.database.model.MetaDataEntity
import com.pgillis.dream.core.model.MetaData

fun MetaData.asEntity(bookId: String) = MetaDataEntity(bookId, title, language, creator, coverId)