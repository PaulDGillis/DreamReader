plugins {
    alias(libs.plugins.dream.android.library)
    alias(libs.plugins.dream.hilt)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.pgillis.paper.file"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.model)
    implementation(libs.okio)
    implementation(libs.ksoup)
    testImplementation(libs.kotlinx.coroutines.test)
}