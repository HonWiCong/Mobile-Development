package com.example.shopnow.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shopnow.fragment.*

class MyPurchaseViewpagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)  {

    private val tabTitles = listOf("To Ship", "To Receive", "Completed", "Cancelled")

    override fun getItemCount(): Int {
        return tabTitles.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> { ToShipFragment.newInstance(tabTitles[position]) }
            1-> { ToReceiveFragment.newInstance(tabTitles[position]) }
            2-> { CompletedFragment.newInstance(tabTitles[position]) }
            3-> { CancelledFragment.newInstance(tabTitles[position]) }
            else -> { ToShipFragment.newInstance(tabTitles[position]) }
        }
    }

    fun getTabTitle(position: Int): String {
        return tabTitles[position]
    }
}