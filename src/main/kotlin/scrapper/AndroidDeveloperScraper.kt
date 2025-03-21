package scrapper

import com.microsoft.playwright.*
import com.microsoft.playwright.options.WaitForSelectorState
import data.NewsArticle

//scraper for the web page : https://developer.android.com/news
class AndroidDeveloperScraper {

    suspend fun scrapeData(): List<NewsArticle> {
        val url = "https://developer.android.com/news"
        val newsArticles: MutableList<NewsArticle> = mutableListOf()

        // Initialize Playwright
        Playwright.create().use { playwright ->
            // Launch a browser (run in non-headless mode for debugging)
            playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(false)).use { browser ->
                // Create a new page
                browser.newPage().use { page ->
                    // Navigate to the URL
                    page.navigate(url)

                    // Wait for the articles to load
                    page.waitForSelector(
                        "div.devsite-card-wrapper",
                        Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000.0)
                    )

                    // Extract all news articles
                    val articles = page.querySelectorAll("div.devsite-card-wrapper")

                    // Log the number of articles found
                    println("Number of articles found: ${articles.size}")

                    for (article in articles) {
                        val title = article.querySelector("h3.no-link.hide-from-toc")?.textContent()?.trim()
                        val link = article.querySelector("a")?.getAttribute("href")?.trim()
                        val fullLink = if (link?.startsWith("/") == true) "https://developer.android.com$link" else link
                        val description = article.querySelector("p.devsite-card-summary")?.textContent()?.trim()
                        val date = article.querySelector("p.devsite-card-date")?.textContent()?.trim()
                        val imageUrl = article.querySelector("a.devsite-card-image-container img.devsite-card-image")?.getAttribute("src")?.trim()

                        if (title != null && fullLink != null && description != null && date != null && imageUrl != null) {
                            newsArticles.add(
                                NewsArticle(
                                    title = title,
                                    link = fullLink,
                                    description = description,
                                    date = date,
                                    imageUrl = imageUrl
                                )
                            )
                        }
                    }
                }
            }
        }

        return newsArticles
    }
}