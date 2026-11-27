package com.example.recipebook

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = findViewById<RecyclerView>(R.id.main_recipes_list)
        val titleInput = findViewById<EditText>(R.id.main_recipe_title)
        val descInput = findViewById<EditText>(R.id.main_recipe_description)
        val savoryButton = findViewById<Button>(R.id.main_add_savory_button)
        val sweetButton = findViewById<Button>(R.id.main_add_sweet_button)

        adapter = RecipeAdapter { recipe ->
            AlertDialog.Builder(this)
                .setTitle(recipe.title)
                .setMessage(recipe.description)
                .setPositiveButton("OK", null)
                .show()
        }
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        fun addRecipe(flavor: Flavor) {
            val title = titleInput.text.toString().trim()
            val desc = descInput.text.toString().trim()
            if (title.isEmpty()) {
                titleInput.error = "Please enter a title"
                return
            }
            adapter.addRecipe(Recipe(title, desc, flavor))
            titleInput.text.clear()
            descInput.text.clear()
        }
        savoryButton.setOnClickListener { addRecipe(Flavor.SAVORY) }
        sweetButton.setOnClickListener { addRecipe(Flavor.SWEET) }

        val swipeHelper = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun getSwipeDirs(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder
            ): Int = if (vh is FlavorTitleViewHolder) 0 else super.getSwipeDirs(rv, vh)

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeAt(vh.bindingAdapterPosition)
            }
        }
        ItemTouchHelper(swipeHelper).attachToRecyclerView(list)
    }
}