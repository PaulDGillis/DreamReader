plugins {
    alias(libs.plugins.dream.jvm.library)
    id("kotlinx-serialization")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}