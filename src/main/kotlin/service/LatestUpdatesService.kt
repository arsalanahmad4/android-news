package service

import model.CategorizedUpdates

object LatestUpdatesService {

    // Store the scraped data in a mutable map
    private val latestUpdates = mutableMapOf<String, CategorizedUpdates>()

    /**
     * Get all the latest updates.
     * @return A map of categories to their updates.
     */
    fun getLatestUpdates(): Map<String, CategorizedUpdates> = latestUpdates

    /**
     * Get updates for a specific category.
     * @param category The category to retrieve updates for.
     * @return The updates for the specified category, or null if the category doesn't exist.
     */
    fun getUpdatesForCategory(category: String): CategorizedUpdates? = latestUpdates[category]

    /**
     * Add or update scraped data.
     * @param scrapedData The scraped data to add or update.
     */
    fun addOrUpdateScrapedData(scrapedData: Map<String, CategorizedUpdates>) {
        latestUpdates.clear()
        latestUpdates.putAll(scrapedData)
    }

    /**
     * Clear all the stored updates.
     */
    fun clearUpdates() {
        latestUpdates.clear()
    }
}