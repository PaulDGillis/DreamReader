plugins {
    alias(libs.plugins.dream.android.library)
    alias(libs.plugins.dream.android.room)
    alias(libs.plugins.dream.koin)
    id("kotlinx-serialization")
}

android {
    namespace = "com.pgillis.dream.core.database"
}

dependencies {
    api(projects.core.model)
    implementation(libs.kotlinx.serialization.json)
}