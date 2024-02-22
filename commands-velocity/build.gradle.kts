plugins {
    kotlin("kapt")
}

group = "cc.fyre.kt"

dependencies {
    kapt(libs.velocity.api)
    implementation(project(":commands"))

    compileOnly(libs.cache4k)

    compileOnly(libs.minimessage.api)
    compileOnly(libs.minimessage.text)

    compileOnly(libs.mccoroutine.velocity.api)
    compileOnly(libs.mccoroutine.velocity.core)

    compileOnly(libs.velocity.api)
}