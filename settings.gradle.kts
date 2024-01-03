@file:Suppress("UnstableApiUsage")

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

