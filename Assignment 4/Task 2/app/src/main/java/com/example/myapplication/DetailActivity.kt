package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val image = intent.getParcelableExtra("Image") as Image?
        val imageView = findViewById<ImageView>(R.id.imageCover)

        Glide.with(this)
            .load(image?.image)
            .into(imageView)

        val photographerText = findViewById<TextView>(R.id.photographer_text)
        photographerText.text = "Photographer: ${image?.photographer}"

        val categoryText = findViewById<TextView>(R.id.category_text)
        categoryText.text = "Category: ${image?.category?.capitalize()}"

    }
}