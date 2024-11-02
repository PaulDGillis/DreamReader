plugins {
    alias(libs.plugins.dream.android.library)
    alias(libs.plugins.dream.hilt)
}

android {
    namespace = "com.pgillis.dream.core.datastore"
}

dependencies {
    api(projects.core.model)
    implementation(libs.datastore)
}