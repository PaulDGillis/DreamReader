import org.gradle.kotlin.dsl.android

plugins {
    alias(libs.plugins.dream.kotlin.multiplatform)
    alias(libs.plugins.dream.compose.multiplatform)
}

android {
    namespace = "com.pgillis.dream.core.designsystem"
}

//dependencies {
//    api(libs.androidx.compose.material3)
//    api(libs.androidx.compose.material3.adaptive)
//}
