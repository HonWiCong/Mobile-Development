package com.example.shopnow.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.shopnow.R
import com.example.shopnow.adapter.HomepageViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        val viewPagerAdapter = HomepageViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout,viewPager2){tab, position->
            when(position){
                0->{ tab.text="All" }
                1->{ tab.text="Food" }
                2->{ tab.text="Technology" }
                3->{ tab.text="Daily" }
            }
        }.attach()

        return view
    }

}