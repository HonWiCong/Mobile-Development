package com.example.shopnowseller.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnowseller.R
import com.example.shopnowseller.adapter.OrderRecyclerViewAdapter
import com.example.shopnowseller.data_class.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class OrderActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val recyclerView = findViewById<RecyclerView>(R.id.order_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val database = FirebaseFirestore.getInstance()
        val orderList = ArrayList<Order>()
        val id = FirebaseAuth.getInstance().currentUser!!.uid

        database
            .collection("orders")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val order = document.toObject<Order>()
                    if (order.seller_id == id) {
                        orderList.add(order)
                    }
                }
                recyclerView.adapter = OrderRecyclerViewAdapter(orderList, this)
            }

    }
}