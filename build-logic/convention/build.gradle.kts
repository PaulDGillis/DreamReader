/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    `kotlin-dsl`
}

group = "com.pgillis.dream.buildlogic"

dependencies {
    compileOnly(libs.plugins.android.application.toDep())
    compileOnly(libs.plugins.android.library.toDep())
    compileOnly(libs.plugins.compose.multiplatform.toDep())
    compileOnly(libs.plugins.kotlin.multiplatform.toDep())
    compileOnly(libs.plugins.compose.compiler.toDep())
    compileOnly(libs.plugins.ksp.toDep())
    compileOnly(libs.plugins.room.toDep())
    implementation(libs.truth)
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "dream.android.app"
            implementationClass = "AndroidAppConventionPlugin"
        }
        register("feature") {
            id = "dream.feature"
            implementationClass = "FeatureConventionPlugin"
        }
        register("koin") {
            id = "dream.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("room") {
            id = "dream.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("kotlin"){
            id = "dream.kotlin"
            implementationClass = "KotlinConventionPlugin"
        }
        register("compose"){
            id = "dream.compose"
            implementationClass = "ComposeConventionPlugin"
        }
    }
}
