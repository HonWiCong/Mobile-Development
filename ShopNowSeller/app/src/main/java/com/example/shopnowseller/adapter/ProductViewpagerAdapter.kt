package com.example.shopnowseller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shopnowseller.fragment.*

class ProductViewpagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)  {

    private val tabTitles = listOf("All", "Out of stock", "Inactive")

    override fun getItemCount(): Int {
        return tabTitles.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> { AllProductFragment.newInstance(tabTitles[position]) }
            1-> { ProductOutOfStockFragment.newInstance(tabTitles[position]) }
            2-> { InactiveFragment.newInstance(tabTitles[position]) }
            else -> { AllProductFragment.newInstance(tabTitles[position]) }
        }
    }

    fun getTabTitle(position: Int): String {
        return tabTitles[position]
    }
}