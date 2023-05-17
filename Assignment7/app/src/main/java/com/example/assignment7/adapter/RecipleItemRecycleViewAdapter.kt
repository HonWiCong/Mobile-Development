package com.example.assignment7.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment7.IngredientActivity
import com.example.assignment7.R
import com.example.assignment7.data_class.Recipe

class RecipeItemRecycleViewAdapter(private val recipeList: ArrayList<Recipe>, private val context: Context) : RecyclerView.Adapter<RecipeItemRecycleViewAdapter.RecipeItemViewHolder>() {
    class RecipeItemViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image = ItemView.findViewById<ImageView>(R.id.item_image)
        val title = ItemView.findViewById<TextView>(R.id.item_title_text)
        val source = ItemView.findViewById<TextView>(R.id.item_source_text)
        val diet = ItemView.findViewById<TextView>(R.id.item_diet_text)
        val button = ItemView.findViewById<Button>(R.id.item_ingredient_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.recipe_item, parent, false)
        return RecipeItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeItemViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.title.text = recipe.label.toString()
        holder.source.text = recipe.source.toString()
        holder.diet.text = recipe.dietLabels.joinToString(separator = ",")

        Glide.with(context)
            .load(recipe.images?.THUMBNAIL?.url.toString())
            .fitCenter()
            .centerCrop()
            .override(350, 350)
            .into(holder.image);

        holder.button.setOnClickListener {
            val intent = Intent(context, IngredientActivity::class.java)
            intent.putExtra("label", recipe.label)
            intent.putExtra("image", recipe.image)
            intent.putExtra("ingredients", recipe.ingredients)
            context.startActivity(intent)
        }
    }

}
