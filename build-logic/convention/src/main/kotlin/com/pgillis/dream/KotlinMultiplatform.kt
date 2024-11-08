package com.pgillis.dream

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(17)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")
    macosX64()
    macosArm64()
//  Would support targets if I could, so many libraries are missing these two targets
//    linuxX64()
//    mingwX64()

    listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain {
            dependencies {
                implementation(libs.findLibrary("kotlinx.coroutines.core").get())
            }

            androidMain {
                dependencies {
                    implementation(libs.findLibrary("kotlinx.coroutines.android").get())
                }
            }

            named("desktopMain").dependencies {
//                implementation(libs.findLibrary("kotlinx.coroutines.swing").get())
            }
        }
    }
}