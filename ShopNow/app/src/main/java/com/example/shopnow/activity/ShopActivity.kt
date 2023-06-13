package com.example.shopnow.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.adapter.ProductRecyclerViewAdapter
import com.example.shopnow.data_class.Product
import com.example.shopnow.data_class.Shop
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ShopActivity : AppCompatActivity() {
    private val database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Shop Detail"

        val shopThumbnail = findViewById<ImageView>(R.id.shop_thumbnail)
        val shopCover = findViewById<ImageView>(R.id.shop_cover)
        val shopName = findViewById<TextView>(R.id.shop_name)
        val shopProductRecyclerView = findViewById<RecyclerView>(R.id.shop_product_list)

        val sellerID = intent.getStringExtra("seller_id")!!

        database
            .collection("shops")
            .document(sellerID)
            .get()
            .addOnSuccessListener {
                val shop = it.toObject<Shop>()
                Glide.with(this)
                    .load(shop!!.thumbnail)
                    .into(shopThumbnail)

                Glide.with(this)
                    .load(shop.cover)
                    .into(shopCover)

                shopName.text = shop.name
            }

        val productList = ArrayList<Product>()
        shopProductRecyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        database
            .collection("products")
            .whereEqualTo("status", true)
            .whereEqualTo("seller", sellerID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val productData = document.toObject(Product::class.java)
                    productData.id = document.id
                    productList.add(productData)
                }
                shopProductRecyclerView.adapter = ProductRecyclerViewAdapter(productList, this)
            }
            .addOnFailureListener { Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show() }
    }
}