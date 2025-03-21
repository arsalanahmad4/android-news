package com.adev

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    // Install Content Negotiation with JSON support
    install(ContentNegotiation) {
        json()
    }

    // Install Call Logging
    install(CallLogging) {
        level = org.slf4j.event.Level.INFO // Set the logging level
    }
}
