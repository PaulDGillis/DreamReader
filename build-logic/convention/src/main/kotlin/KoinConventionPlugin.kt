/*
 * Copyright 2023 The Android Open Source Project
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

import com.google.devtools.ksp.gradle.KspExtension
import com.pgillis.dream.kspTargets
import com.pgillis.dream.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.findPlugin("ksp").get().get().pluginId)

            // Add support for Jvm Module, base on org.jetbrains.kotlin.jvm
            pluginManager.withPlugin(libs.findPlugin("kotlin.multiplatform").get().get().pluginId) {
                extensions.configure<KotlinMultiplatformExtension> {
                    sourceSets.apply {
                        commonMain.dependencies {
                            implementation(libs.findLibrary("koin.compose").get())
                            implementation(libs.findLibrary("koin.compose.viewmodel").get())
                            api(libs.findLibrary("koin.annotations").get())
//                        implementation(libs.findLibrary("koin.compose.viewmodel.navigation").get())
                        }
                        androidMain.dependencies {
                            implementation(libs.findLibrary("koin.android").get())
                        }
                    }
                }
            }

            dependencies.apply {
                kspTargets.forEach {
                    add(it, libs.findLibrary("koin.ksp.compiler").get())
                }
            }

            extensions.configure<KspExtension> {
                arg("KOIN_CONFIG_CHECK","true")
                arg("KOIN_USE_COMPOSE_VIEWMODEL","true")
            }
        }
    }
}
