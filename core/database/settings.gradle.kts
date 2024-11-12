pluginManagement {
//    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://packages.jetbrains.team/maven/p/amper/amper")
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    }
}

plugins {
    id("org.jetbrains.amper.settings.plugin").version("0.5.0")
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

//@Suppress("UnstableApiUsage")
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
//}

//rootProject.name = "DreamReader"

//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
//include(":android-app")
//include(":jvm-app")
//include(":ios-app")
//include(":shared")
//include(":database")
//include(":core:datastore")
//include(":core:designsystem")
//include(":core:file")
//include(":core:model")
//include(":feature:library")