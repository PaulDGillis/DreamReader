plugins {
    alias(libs.plugins.dream.android.library)
    alias(libs.plugins.dream.hilt)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.pgillis.paper.file"
    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.model)
    implementation(libs.okio)
    implementation(libs.kotlinx.io)
    implementation(libs.ksoup)
    implementation(libs.simple.storage)
    implementation(libs.kmpfile)
    implementation(libs.kmpfile.okio)
    implementation(libs.androidx.tracing.ktx)

    testImplementation(libs.kotlinx.coroutines.test)
}