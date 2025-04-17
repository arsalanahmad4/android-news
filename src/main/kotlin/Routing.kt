package com.adev

import NewsArticlesService
import model.NewsArticle
import io.ktor.http.*
import scrapper.AndroidDeveloperScraper
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import service.LatestUpdatesService

fun Application.configureRouting() {
    // Define your routes
    routing {

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

        get("/scrape-latest-updates") {
            val scraper = AndroidDeveloperScraper()
            val categorizedUpdates = scraper.scrapeLatestUpdates()
            LatestUpdatesService.addOrUpdateScrapedData(categorizedUpdates)
            call.respond(categorizedUpdates)
        }

        get("/latest-updates") {
            // Retrieve the stored news articles from the service
            call.respond(LatestUpdatesService.getLatestUpdates())
        }
    }
}