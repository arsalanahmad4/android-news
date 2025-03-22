import model.NewsArticle

object NewsArticlesService {

    private val newsArticles = mutableListOf<NewsArticle>()

    fun getNewsArticles(): List<NewsArticle> = newsArticles

    fun createNewsList(newsArticleList: List<NewsArticle>) {
        newsArticles.clear()
        newsArticles.addAll(newsArticleList)
    }

    // Function to add scraped data
    fun addScrapedData(scrapedArticles: List<NewsArticle>) {
        newsArticles.clear()
        newsArticles.addAll(scrapedArticles)
    }
}