import org.gradle.kotlin.dsl.android

plugins {
    alias(libs.plugins.dream.android.library)
    alias(libs.plugins.dream.android.library.compose)
}

android {
    namespace = "com.pgillis.dream.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
}
