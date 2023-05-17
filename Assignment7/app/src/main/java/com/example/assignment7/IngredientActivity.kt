package com.example.assignment7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.assignment7.data_class.Ingredients

class IngredientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)

        val label = intent.getStringExtra("label")
        val image = intent.getStringExtra("image")
        val ingredients : ArrayList<Ingredients> = intent.getParcelableArrayListExtra("ingredients")!!

        val titleText = findViewById<TextView>(R.id.ingredient_title_text)
        val foodImage = findViewById<ImageView>(R.id.ingredient_image)
        val ingredientList = findViewById<TextView>(R.id.ingredient_list)

        titleText.text = label

        Glide.with(this)
            .load(image)
            .fitCenter()
            .centerCrop()
            .into(foodImage);

        val modifiedList = ArrayList<String>()

        for (item in ingredients) {
            modifiedList.add("• ${item.text.toString()}")
            Log.d("Item: ", item.text.toString())
        }

        ingredientList.text = modifiedList.joinToString(separator = "\n")
    }
}