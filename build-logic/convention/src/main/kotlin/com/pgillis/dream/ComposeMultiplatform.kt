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

package com.pgillis.dream

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.model.ComposeCompiler

/**
 * Configure Compose-specific options
 */
internal fun Project.configureKotlinComposeMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    macosX64()
    macosArm64()
    linuxX64()
    mingwX64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = path.substringAfterLast(":")
            isStatic = true
        }
    }

    jvm("desktop")

    pluginManager.withPlugin("org.jetbrains.compose") {
        this.
        val composeDeps = extensions.getByType<ComposeExtension>()
    }


    sourceSets.apply {
        named("desktopMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
            }
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.findLibrary("androidx.activity:activity-compose").get())
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
            implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
        }

    }
}
