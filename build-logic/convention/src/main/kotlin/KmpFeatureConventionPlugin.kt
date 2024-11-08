/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.android.build.gradle.LibraryExtension
import com.pgillis.dream.configureGradleManagedDevices
import com.pgillis.dream.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply(libs.findPlugin("dream.kotlin.multiplatform").get().get().pluginId)
                apply(libs.findPlugin("dream.compose.multiplatform").get().get().pluginId)
                apply(libs.findPlugin("dream.koin").get().get().pluginId)
//                apply(libs.findPlugin("org.jetbrains.kotlin.plugin.serialization").get().get().pluginId)
                apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(project(":core:ui"))
                        implementation(project(":core:designsystem"))

                        //                add("implementation", libs.findLibrary("koin-androidx-compose-navigation").get())
                        //                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                        //                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                        //                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
                        //                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
                        implementation(libs.findLibrary("kotlinx.serialization.json").get())

                        //                add("testImplementation", libs.findLibrary("androidx.navigation.testing").get())
                        //                add("androidTestImplementation", libs.findLibrary("androidx.lifecycle.runtimeTesting").get())
                    }
                }
            }
        }
    }
}
