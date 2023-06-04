package com.example.shopnow.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.adapter.CartRecyclerViewAdapter
import com.example.shopnow.adapter.HomepageViewPagerAdapter
import com.example.shopnow.adapter.ProductRecyclerViewAdapter
import com.example.shopnow.data_class.Product
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewPager2)
        val banner = view.findViewById<ImageView>(R.id.home_page_banner)
        val recyclerView = view.findViewById<RecyclerView>(R.id.home_product_list)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        val productList = ArrayList<Product>()
        val database = FirebaseFirestore.getInstance()

        database
            .collection("products")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val product = document.toObject<Product>()
                    product.id = document.id
                    productList.add(product)
                }
                recyclerView.adapter = ProductRecyclerViewAdapter(productList, view.context)
            }

        Glide.with(this)
            .load("https://images.unsplash.com/photo-1472851294608-062f824d29cc?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80")
            .into(banner)

        val viewPagerAdapter = HomepageViewPagerAdapter(requireActivity())
        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = viewPagerAdapter.getTabTitle(position)
        }.attach()

        return view
    }

}