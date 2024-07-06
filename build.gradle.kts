import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `maven-publish`
    kotlin("jvm") version "2.0.0"
    id("net.linguica.maven-settings") version "0.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cc.fyre.kt"

allprojects {

    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "net.linguica.maven-settings")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        mavenCentral()
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly(kotlin("reflect"))

        compileOnly(rootProject.libs.kotlin.atomic)
        compileOnly(rootProject.libs.kotlin.coroutines)

        testImplementation(kotlin("test"))
    }

    tasks {

        java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))
        kotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_11)

        test {
            useJUnitPlatform()
        }

        shadowJar {
            archiveBaseName.set("kt-commands-${this.project.name.replace("commands-","")}")
            archiveClassifier.set("")
        }

        kotlin {
            compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
            compilerOptions.freeCompilerArgs.set(listOf(
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-Xopt-in=io.ktor.util.KtorExperimentalAPI",
                "-Xextended-compiler-checks",
                "-language-version","2.0",
//                "-Xno-param-assertions",
//                "-XXLanguage:+InlineClasses"
            ))
        }

        publishToMavenLocal {
            dependsOn(shadowJar)
        }
//
    }

    publishing {

        repositories {
            fyreReleases()
        }

        publications {

            create<MavenPublication>("shadow") {

                shadow{
                    component(this@create)
                }

            }

        }

    }
}

fun RepositoryHandler.fyreReleases() {
    maven {
        name = "fyreReleases"
        url = uri("https://maven.fyre.cc/releases")
    }
}