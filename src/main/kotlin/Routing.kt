package com.adev

import NewsArticlesService
import data.NewsArticle
import io.ktor.http.*
import scrapper.AndroidDeveloperScraper
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Define your routes
    routing {
        get("/") {
            call.respondText("Hello, Ktor with Content Negotiation and Logging!")
        }

        get("/scrape-news") {
            val scraper = AndroidDeveloperScraper()
            val newsArticles = scraper.scrapeData()
            // Store the scraped data in the service
            NewsArticlesService.addScrapedData(newsArticles)
            call.respond(newsArticles)
        }

        get("/news") {
            // Retrieve the stored news articles from the service
            call.respond(NewsArticlesService.getNewsArticles())
        }

        post("update/news-list") {
            val updatedNewsList = call.receive<List<NewsArticle>>()
            // Update the news list in the service
            NewsArticlesService.createNewsList(updatedNewsList)
            call.respond(HttpStatusCode.OK, "News list updated successfully")
        }
    }
}