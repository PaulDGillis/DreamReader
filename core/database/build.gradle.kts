//object libs {
//    const val roomVersion = "2.7.0-alpha11"
//    const val roomCompiler = "androidx.room:room-compiler:2.7.0-alpha11$roomVersion"
//}

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("androidx.room") version "2.7.0-alpha11"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    kspCommonMainMetadata("androidx.room:room-compiler:2.7.0-alpha11")
    kspJvm("androidx.room:room-compiler:2.7.0-alpha11")
    kspAndroid("androidx.room:room-compiler:2.7.0-alpha11")
    kspIosSimulatorArm64("androidx.room:room-compiler:2.7.0-alpha11")
}

android {
    namespace = "com.pgillis.dream"
    compileSdkVersion = "android-34"
}
