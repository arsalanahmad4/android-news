package model

import kotlinx.serialization.Serializable

@Serializable
data class NewsArticle(
    val title: String,
    val link: String,
    val description: String,
    val date: String,
    val imageUrl: String
)