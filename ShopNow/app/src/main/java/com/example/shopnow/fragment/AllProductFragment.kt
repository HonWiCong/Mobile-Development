package com.example.shopnow.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shopnow.R
import com.example.shopnow.adapter.ProductRecyclerViewAdapter
import com.example.shopnow.data_class.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AllProductFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductRecyclerViewAdapter
    private val items: MutableList<Product> = mutableListOf()

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_product, container, false)
        recyclerView = view.findViewById(R.id.all_product_list)

        setupRecyclerView()

        fetchItemsFromFirestore()

        return view
    }

    private fun setupRecyclerView() {
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        adapter = ProductRecyclerViewAdapter(items, requireContext())
        recyclerView.adapter = adapter
    }

    private fun fetchItemsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("products")

        collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e(TAG, "Error fetching items: $exception")
                return@addSnapshotListener
            }

            items.clear()
            snapshot?.documents?.forEach { document ->
                val product = document.toObject<Product>()
                product?.id = document.id
                items.add(product!!)
            }

            adapter.notifyDataSetChanged()
        }
    }
}
