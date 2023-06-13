package com.example.shopnow.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shopnow.R
import com.example.shopnow.adapter.ProductRecyclerViewAdapter
import com.example.shopnow.data_class.Product
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductRecyclerViewAdapter
    private val items: MutableList<Product> = mutableListOf()
    private lateinit var searchBox : EditText

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Search"

        recyclerView = findViewById(R.id.search_list)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        searchBox = findViewById(R.id.search_box)
        val searchButton = findViewById<Button>(R.id.search_button)
        setupRecyclerView()


        searchButton.setOnClickListener {
            fetchItemsFromFirestore()
        }

    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        adapter = ProductRecyclerViewAdapter(items, this)
        recyclerView.adapter = adapter
    }

    private fun fetchItemsFromFirestore() {
        val searchText = searchBox.text.toString()
        val database = FirebaseFirestore.getInstance()
        database
            .collection("products")
            .orderBy("name")
            .startAt(searchText)
            .endAt(searchText + '\uf8ff')
            .get()
            .addOnSuccessListener { documents ->
                items.clear()
                for (document in documents) {
                    val product = document.toObject<Product>()
                    product.id = document.id
                    items.add(product)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

}