package com.pgillis.dream.core.data.model

import com.pgillis.dream.core.database.model.MetaDataEntity
import com.pgillis.dream.core.model.MetaData

fun MetaDataEntity.asMetaData() = MetaData(title, language, creator)