plugins {
    alias(libs.plugins.dream.compose.feature)
}

android {
    namespace = "com.pgillis.dream.feature.library"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.database)
            api(projects.core.datastore)
            api(projects.core.file)
//            debugImplementation(libs.androidx.compose.ui.tooling)
//    implementation(libs.androidx.lifecycle.runtimeCompose)

            // Compose Placeholder
            implementation(libs.compose.placeholder)

            // Compose File UI Picker
            implementation(libs.filekit)
            implementation(libs.kmpfile)
            implementation(libs.kmpfile.filekit)

            // Compose Image Loading
            implementation(libs.coil.compose)
//            implementation(libs.coil.network)
        }
    }
}