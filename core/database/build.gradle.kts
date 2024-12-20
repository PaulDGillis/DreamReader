plugins {
    alias(libs.plugins.dream.kotlin.multiplatform)
    alias(libs.plugins.dream.android.room)
    alias(libs.plugins.dream.koin)
    id("kotlinx-serialization")
}

android {
    namespace = "com.pgillis.dream.core.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            implementation(libs.kotlinx.serialization.json)
        }
    }
}