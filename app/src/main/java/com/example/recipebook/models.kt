package com.example.recipebook

enum class Flavor(val label: String) {
    SAVORY("Savory"),
    SWEET("Sweet")
}

data class Recipe(
    val title: String,
    val description: String,
    val flavor: Flavor
)

sealed class ListItem {
    data class FlavorTitle(val flavor: Flavor) : ListItem()
    data class RecipeItem(val recipe: Recipe) : ListItem()
}