package com.example.shopnowseller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnowseller.R
import com.example.shopnowseller.adapter.ProductRecyclerViewAdapter
import com.example.shopnowseller.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AllFragment : Fragment() {
    val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.all_product_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val products = ArrayList<Product>()

        val currentUser = FirebaseAuth.getInstance().currentUser
        database
            .collection("products")
            .whereEqualTo("status", true)
            .whereEqualTo("seller", currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val productData = document.toObject(Product::class.java)
                    productData.id = document.id
                    products.add(productData)
                }
                recyclerView.adapter = ProductRecyclerViewAdapter(products, view.context)
            }
            .addOnFailureListener { Toast.makeText(view.context, "Something Wrong", Toast.LENGTH_SHORT).show() }

        return view
    }

}