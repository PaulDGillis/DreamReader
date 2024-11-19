plugins {
    alias(libs.plugins.dream.kotlin)
    alias(libs.plugins.dream.koin)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.pgillis.dream.core.file"
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
            implementation(libs.ksoup)

        //    implementation(libs.androidx.tracing.ktx)

//            testImplementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
//            implementation(libs.simple.storage)
            implementation(libs.document.file)
        }
    }
}
dependencies {
    implementation(project(":shared"))
}
