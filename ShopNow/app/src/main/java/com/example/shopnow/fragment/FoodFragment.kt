package com.example.shopnow.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shopnow.R
import com.example.shopnow.adapter.ProductRecyclerViewAdapter
import com.example.shopnow.data_class.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class FoodFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.food_list)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)

        val newProductList = ArrayList<Product>()
        val database = FirebaseFirestore.getInstance()
        database
            .collection("products")
            .whereEqualTo("status", true)
            .whereEqualTo("category", "Food")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val product = document.toObject<Product>()
                    newProductList.add(product)
                }
                recyclerView.adapter = ProductRecyclerViewAdapter(newProductList, view.context)
            }

        return view
    }

}