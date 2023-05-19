package com.example.shopnow.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shopnow.R
import com.example.shopnow.adapter.ProductRecyclerViewAdapter
import com.example.shopnow.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AllProductFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val productsCollection = db.collection("products")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_product, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.all_product_list)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        val products = ArrayList<Product>()

        productsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val productData = document.toObject(Product::class.java)
                    productData.id = document.id
                    products.add(productData)
                }
                recyclerView.adapter = ProductRecyclerViewAdapter(products, view.context)
            }
            .addOnFailureListener { e ->
                println("Error getting products: $e")
            }


        return view
    }
}