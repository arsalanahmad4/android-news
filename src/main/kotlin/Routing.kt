package com.adev

import AndroidDeveloperScraper
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Define your routes
    routing {
        get("/") {
            call.respondText("Hello, Ktor with Content Negotiation and Logging!")
        }

        get("/news") {
            val scraper = AndroidDeveloperScraper()
            val newsArticles = scraper.scrapeData()
            call.respond(newsArticles)
        }
    }
}
