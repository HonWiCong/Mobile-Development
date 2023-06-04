package com.example.shopnow.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.adapter.PurchasedRecyclerViewAdapter
import com.example.shopnow.data_class.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ToShipActivity : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseFirestore.getInstance()
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_ship)

        toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val recyclerView = findViewById<RecyclerView>(R.id.to_ship_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val orderList = ArrayList<Order>()
        database
            .collection("orders")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val order = document.toObject<Order>()
                    if (order.buyer_id == currentUser!!.uid) {
                        orderList.add(order)
                    }
                }
                recyclerView.adapter = PurchasedRecyclerViewAdapter(orderList, this)
            }

    }
}