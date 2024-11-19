plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            implementation(libs.sqlite.bundle)
        }
        val jvmMain by getting {
            // Configure the source set here
        }
    }
}

android {
    namespace = "com.pgillis.dream.core.database"
}