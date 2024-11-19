plugins {
    alias(libs.plugins.dream.kotlin)
    id("kotlinx-serialization")
}

android {
    namespace = "com.pgillis.dream.model"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
