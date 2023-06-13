package com.example.shopnowseller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shopnowseller.fragment.OrderCancelledFragment
import com.example.shopnowseller.fragment.OrderCompletedFragment
import com.example.shopnowseller.fragment.OrderToArriveFragment
import com.example.shopnowseller.fragment.OrderToShipFragment

class OrderViewpagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)  {

    private val tabTitles = listOf("To Ship", "To Arrive", "Completed", "Cancelled")

    override fun getItemCount(): Int {
        return tabTitles.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> { OrderToShipFragment.newInstance(tabTitles[position]) }
            1-> { OrderToArriveFragment.newInstance(tabTitles[position]) }
            2-> { OrderCompletedFragment.newInstance(tabTitles[position]) }
            3-> { OrderCancelledFragment.newInstance(tabTitles[position]) }
            else -> { OrderToShipFragment.newInstance(tabTitles[position]) }
        }
    }

    fun getTabTitle(position: Int): String {
        return tabTitles[position]
    }
}