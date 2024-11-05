plugins {
    alias(libs.plugins.dream.android.library)
    alias(libs.plugins.dream.hilt)
}

android {
    namespace = "com.pgillis.dream.core.data"
}

dependencies {
    api(projects.core.datastore)
    api(projects.core.database)
    api(projects.core.file)
    api(projects.core.model)
    implementation(libs.okio)
    implementation(libs.filekit)
    implementation(libs.kmpfile)
    implementation(libs.kmpfile.filekit)
}