plugins {
    alias(libs.plugins.dream.kotlin.multiplatform)
    alias(libs.plugins.dream.koin)
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

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
            implementation(libs.okio)
            implementation(libs.kotlinx.io)
            implementation(libs.ksoup)

            implementation(libs.kmpfile)
            implementation(libs.kmpfile.okio)
        //    implementation(libs.androidx.tracing.ktx)

//            testImplementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.simple.storage)
        }
    }
}
