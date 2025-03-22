package model

import kotlinx.serialization.Serializable

@Serializable
data class Update(
    val title: String,
    val link: String?,
    val description: String,
    val date: String,
    val category: String,
    val tag : String?
)

@Serializable
data class CategoryDetails(
    val iconUrl: String,
    val description: String
)

@Serializable
data class CategorizedUpdates(
    val categoryDetails: CategoryDetails,
    val updates: List<Update>
)