@file:Suppress("UnstableApiUsage")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "KT-Commands"

include(":commands")
include("commands-bukkit")

dependencyResolutionManagement {
    versionCatalogs {
        register("libs") {
            from(files("libs.versions.toml"))
        }
    }
}
include("commands-velocity")
