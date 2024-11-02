package com.pgillis.dream.core.model

data class Book(
    val id: String,
    val metaData: MetaData,
    val manifest: Map<String, String>,
    val spine: LinkedHashSet<String>
)