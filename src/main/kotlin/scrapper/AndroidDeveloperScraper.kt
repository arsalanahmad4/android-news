package scrapper

import com.microsoft.playwright.*
import com.microsoft.playwright.options.LoadState
import com.microsoft.playwright.options.WaitForSelectorState
import model.CategorizedUpdates
import model.CategoryDetails
import model.NewsArticle
import model.Update

class AndroidDeveloperScraper {

    //scraper for the web page : https://developer.android.com/news

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
                        val imageUrl = article.querySelector("a.devsite-card-image-container img.devsite-card-image")
                            ?.getAttribute("src")?.trim()

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

    //scraper for the web page : https://developer.android.com/latest-updates
    suspend fun scrapeLatestUpdates(): Map<String, CategorizedUpdates> {
        val url = "https://developer.android.com/latest-updates"
        val categorizedUpdates: MutableMap<String, CategorizedUpdates> = mutableMapOf()

        // Initialize Playwright
        Playwright.create().use { playwright ->
            // Launch a browser (run in non-headless mode for debugging)
            playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(false)).use { browser ->
                // Create a new page
                browser.newPage().use { page ->
                    // Navigate to the URL
                    page.navigate(url)

                    // Wait for the page to load completely
                    page.waitForLoadState(LoadState.NETWORKIDLE) // Wait for network activity to settle

                    // Wait for the updates to load
                    try {
                        page.waitForSelector(
                            "div.devsite-landing-row-inner", // Updated selector
                            Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(60000.0) // Increased timeout
                        )
                    } catch (e: TimeoutError) {
                        println("Timed out waiting for the content to load. Check the selector or network conditions.")
                        return categorizedUpdates
                    }

                    // Extract all sections
                    val sections = page.querySelectorAll("div.devsite-landing-row-inner")

                    for (section in sections) {
                        // Extract the category name from the section header
                        val category = section.querySelector("h2")?.textContent()?.trim() ?: continue

                        // Extract the category icon URL
                        val iconUrl = section.querySelector("picture img, img")?.getAttribute("src")?.trim()
                        val fullIconUrl =
                            if (iconUrl?.startsWith("/") == true) "https://developer.android.com$iconUrl" else iconUrl

                        // Extract the category description
                        val description =
                            section.querySelector("div.devsite-landing-row-description")?.textContent()?.trim()

                        // If icon URL or description is missing, skip this section
                        if (fullIconUrl == null || description == null) continue

                        // Extract all updates in this section
                        val updates = mutableListOf<Update>()
                        val updateContainers = section.querySelectorAll("div.devsite-landing-row-item")

                        for (updateContainer in updateContainers) {
                            val title = updateContainer.querySelector("h3, h2")?.textContent()?.trim()
                            val link = updateContainer.querySelector("a")?.getAttribute("href")?.trim()
                            val fullLink =
                                if (link?.startsWith("/") == true) "https://developer.android.com$link" else link
                            val date =
                                updateContainer.querySelector("span.devsite-landing-row-item-labels")?.textContent()?.trim()
                            val updateDescription =
                                updateContainer.querySelector("div.devsite-landing-row-item-description-content")?.textContent()
                                    ?.trim()

                            if (title != null && fullLink != null && date != null && updateDescription != null) {
                                updates.add(
                                    Update(
                                        title = title,
                                        link = fullLink,
                                        description = updateDescription,
                                        date = date,
                                        category = category,
                                        tag = null
                                    )
                                )
                            }
                        }

                        // Add the category and its updates to the map
                        categorizedUpdates[category] = CategorizedUpdates(
                            categoryDetails = CategoryDetails(
                                iconUrl = fullIconUrl,
                                description = description
                            ),
                            updates = updates
                        )
                    }
                }
            }
        }

        return categorizedUpdates
    }

}