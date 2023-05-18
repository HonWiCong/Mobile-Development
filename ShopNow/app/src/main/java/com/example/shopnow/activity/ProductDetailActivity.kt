package com.example.shopnow.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.data_class.Product

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val product : Product = intent.getParcelableExtra("product")!!

        val image = findViewById<ImageView>(R.id.detail_product_image)
        val name = findViewById<TextView>(R.id.detail_product_name)
        val price = findViewById<TextView>(R.id.detail_product_price)
        val rating = findViewById<TextView>(R.id.detail_product_rating)
        val description = findViewById<TextView>(R.id.detail_product_description)

        name.text = product.name

        Glide.with(this)
            .load(product.image)
            .into(image);

        price.text = product.price.toString()
        rating.text = "Rate: ${product.rating}"
        description.text = product.description
    }
}