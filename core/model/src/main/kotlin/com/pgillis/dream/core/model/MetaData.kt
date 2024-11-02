package com.pgillis.dream.core.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    val title: String,
    val language: String,
    val creator: String,
    val coverId: String? = null
)