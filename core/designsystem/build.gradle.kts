plugins {
    alias(libs.plugins.dream.kotlin)
    alias(libs.plugins.dream.compose)
}

android {
    namespace = "com.pgillis.dream.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
}
