package com.example.assignment4_task1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.assignment4_task1.R
import com.example.assignment4_task1.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NewsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.pager)

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout,viewPager2){tab, position->
            when(position){
                0->{ tab.text="SCIENCE" }
                1->{ tab.text="TECHNOLOGY" }
            }
        }.attach()

        return view
    }

}