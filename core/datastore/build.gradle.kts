plugins {
    alias(libs.plugins.dream.kotlin.multiplatform)
    alias(libs.plugins.dream.koin)
}

android {
    namespace = "com.pgillis.dream.core.datastore"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            api(libs.androidx.datastore.preferences.core)
            api(libs.androidx.datastore.core.okio)
        }
    }
}