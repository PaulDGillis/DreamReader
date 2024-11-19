pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://packages.jetbrains.team/maven/p/amper/amper")
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    }
}

plugins {
    id("org.jetbrains.amper.settings.plugin").version("1.0-SNAPSHOT")
}

include(":shared")
include(":feature:library")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:file")
include(":core:model")
//include(":core:ui")
include(":android-app")
include(":desktop-app")
include(":ios-app")