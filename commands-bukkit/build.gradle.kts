import java.io.ByteArrayOutputStream

plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "cc.fyre.kt"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":commands"))

    compileOnly(libs.spigot.api)

    compileOnly(libs.minimessage.api)
    compileOnly(libs.minimessage.text)
    compileOnly(libs.minimessage.platform.bukkit)

    compileOnly(libs.mccoroutine.bukkit.api)
    compileOnly(libs.mccoroutine.bukkit.core)
}

bukkit {
    main = "cc.fyre.kt.commands.bukkit.BukkitCommandPlugin"
    name = "KTCommands"
    website = "https://fyre.cc"
    authors = listOf("Fyre Services")
    version = project.version.toString()

    var commitId = project.findProperty("gitCommit")?.toString()

    if (commitId.isNullOrBlank()) {
        commitId = try {
            getGitHash()
        } catch (ex: Exception) {
            "N/A"
        }

    }

    var runId = project.findProperty("runNumber")?.toString()

    if (runId.isNullOrBlank()) {
        runId = "Local Build"
    }

    version += " "
    version += "["
    version += "Git-${commitId}"
    version += " "
    version += "#${runId}"
    version += "]"
}

fun getGitHash():String {

    val output = ByteArrayOutputStream()

    exec{
        standardOutput = output // Only the std, not errors as it's not to worry about as long as it's a repo
        commandLine = listOf("git", "rev-parse", "--short", "HEAD")
    }

    return output.toString().trim()
}