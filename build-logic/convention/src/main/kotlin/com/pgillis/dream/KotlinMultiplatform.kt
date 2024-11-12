package com.pgillis.dream

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
internal fun Project.configureKotlinMultiplatform(
    extension: KotlinMultiplatformExtension
) = extension.apply {
    jvmToolchain(21)

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")
//    I want to support these targets, but many libraries are missing these targets sadly
//    Also seems material3 and compose in general doesn't really have great native desktop support
//    macosX64()
//    macosArm64()
//    linuxX64()
//    mingwX64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            optimized = true
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets.apply {
        commonMain.dependencies {
            implementation(libs.findLibrary("kotlinx.coroutines.core").get())
        }

        androidMain.dependencies {
            runtimeOnly(libs.findLibrary("kotlinx.coroutines.android").get())
//            coreLibraryDesugaring(libs.findLibrary("android.desugarJdkLibs").get())
        }

        named("desktopMain").dependencies {
            runtimeOnly(libs.findLibrary("kotlinx.coroutines.swing").get())
        }
    }
}