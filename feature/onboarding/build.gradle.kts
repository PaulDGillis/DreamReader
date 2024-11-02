plugins {
    alias(libs.plugins.dream.android.feature)
    alias(libs.plugins.dream.android.library.compose)
}

android {
    namespace = "com.pgillis.dream.feature.onboarding"
}

dependencies {
    implementation(libs.accompanist.permissions)
}