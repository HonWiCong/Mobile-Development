package com.example.shopnowseller.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.shopnowseller.R
import com.example.shopnowseller.activity.AddActivity
import com.example.shopnowseller.adapter.ProductViewpagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        val tabLayout = view.findViewById<TabLayout>(R.id.productTabLayout)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.productViewPager)
        val viewPagerAdapter = ProductViewpagerAdapter(requireActivity())
        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = viewPagerAdapter.getTabTitle(position)
        }.attach()

        // Get tab_layout_tab from the intent and navigate accordingly
        val tabLayoutTab: Int = activity?.intent?.getIntExtra("tab_layout_tab", 0) ?: 0
        viewPager2.currentItem = tabLayoutTab

        val floatingActionButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            val intent = Intent(context, AddActivity::class.java)
            startActivity(intent)
        }

        return view
    }


}