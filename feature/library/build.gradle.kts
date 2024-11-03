plugins {
    alias(libs.plugins.dream.android.feature)
    alias(libs.plugins.dream.android.library.compose)
}

android {
    namespace = "com.pgillis.dream.feature.library"
}

dependencies {
    api(projects.core.data)
    api(projects.core.datastore)
    api(projects.core.file)
    implementation(libs.okio)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Compose File UI Picker
    implementation(libs.filekit)

    // Compose Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
}