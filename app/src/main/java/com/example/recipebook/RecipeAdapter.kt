package com.example.recipebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlavorTitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.flavor_title)
}

class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.recipe_title)
}

class RecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val recipes = mutableListOf<Recipe>()
    private var items = listOf<ListItem>()

    init {
        rebuild()
    }

    private fun rebuild() {
        val list = mutableListOf<ListItem>()
        for (flavor in Flavor.values()) {
            list.add(ListItem.FlavorTitle(flavor))
            recipes.filter { it.flavor == flavor }
                .forEach { list.add(ListItem.RecipeItem(it)) }
        }
        items = list
        notifyDataSetChanged()
    }

    fun addRecipe(recipe: Recipe) {
        recipes.add(recipe)
        rebuild()
    }

    fun removeAt(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        val item = items[position]
        if (item is ListItem.RecipeItem) {
            recipes.remove(item.recipe)
            rebuild()
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (items[position] is ListItem.FlavorTitle) TYPE_FLAVOR else TYPE_RECIPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_FLAVOR) {
            FlavorTitleViewHolder(inflater.inflate(R.layout.item_flavor_title, parent, false))
        } else {
            RecipeViewHolder(inflater.inflate(R.layout.item_recipe, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.FlavorTitle -> {
                (holder as FlavorTitleViewHolder).title.text = item.flavor.label
            }
            is ListItem.RecipeItem -> {
                val h = holder as RecipeViewHolder
                h.title.text = item.recipe.title
                h.itemView.setOnClickListener { onRecipeClick(item.recipe) }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    companion object {
        private const val TYPE_FLAVOR = 0
        private const val TYPE_RECIPE = 1
    }
}