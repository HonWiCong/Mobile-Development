package com.example.shopnowseller.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnowseller.R
import com.example.shopnowseller.activity.AddActivity
import com.example.shopnowseller.adapter.ProductRecyclerViewAdapter
import com.example.shopnowseller.data_class.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AllProductFragment : Fragment() {
    private val database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_product, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.all_product_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val products = ArrayList<Product>()

        val currentUser = FirebaseAuth.getInstance().currentUser
        database
            .collection("products")
            .whereEqualTo("status", true)
            .whereEqualTo("seller", currentUser!!.uid)
            .whereGreaterThan("quantity", 0)
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

    companion object {
        private const val ARG_TAB_TITLE = "arg_tab_title"

        fun newInstance(tabTitle: String): AllProductFragment {
            val fragment = AllProductFragment()
            val args = Bundle()
            args.putString(ARG_TAB_TITLE, tabTitle)
            fragment.arguments = args
            return fragment
        }
    }
}