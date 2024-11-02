package com.pgillis.dream.core.model

data class Settings(
    val theme: Theme = Theme.FollowSystem,
    val libraryDir: String? = null
) {
    companion object {
        val Default = Settings()
    }

    enum class Theme {
        FollowSystem,
        Day,
        Night
    }
}