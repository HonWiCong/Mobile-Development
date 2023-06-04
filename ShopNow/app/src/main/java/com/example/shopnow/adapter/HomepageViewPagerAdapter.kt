package com.example.shopnow.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shopnow.fragment.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomepageViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)  {

    private val tabTitles = listOf("All", "Technology", "Food", "Daily")

    override fun getItemCount(): Int {
        return tabTitles.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> { AllProductFragment.newInstance(tabTitles[position]) }
            1-> { TechnologyFragment.newInstance(tabTitles[position]) }
            2-> { FoodFragment.newInstance(tabTitles[position]) }
            3-> { DailyFragment.newInstance(tabTitles[position]) }
            else -> { AllProductFragment.newInstance(tabTitles[position]) }
        }
    }

    fun getTabTitle(position: Int): String {
        return tabTitles[position]
    }
}
