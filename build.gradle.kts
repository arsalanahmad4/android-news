
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)

}

group = "com.adev"
version = "0.0.1"

application {
    mainClass = "com.adev.ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)

    // Add these for Content Negotiation and Logging
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.call.logging)

    // Add these for Jsoup and Logback
    implementation(libs.jsoup)
    implementation(libs.logback.classic)

    implementation("com.microsoft.playwright:playwright:1.30.0")


    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
