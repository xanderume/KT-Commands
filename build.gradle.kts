import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `maven-publish`
    kotlin("jvm") version "1.9.20"
    id("net.linguica.maven-settings") version "0.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cc.fyre.kt"
version = "1.0.0"

allprojects {

    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "net.linguica.maven-settings")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
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
            fyre()
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

fun RepositoryHandler.fyre() {
    maven {
        name = "fyre"
        url = uri("https://maven.pkg.jetbrains.space/fyre/p/fyre-games/maven")
        applySpaceCredentials()
    }
}

fun MavenArtifactRepository.applySpaceCredentials() {

    if (System.getenv().containsKey("JB_SPACE_CLIENT_ID") && System.getenv().containsKey("JB_SPACE_CLIENT_SECRET")) {
        credentials {
            // Automation has a special account for authentication in Space
            // account credentials are accessible via env vars
            username = System.getenv()["JB_SPACE_CLIENT_ID"]
            password = System.getenv()["JB_SPACE_CLIENT_SECRET"]
        }
    }

}