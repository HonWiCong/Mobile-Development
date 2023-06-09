package com.example.assignment7.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignment7.fragment.BreakfastFragment
import com.example.assignment7.fragment.DinnerFragment
import com.example.assignment7.fragment.LunchFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> { BreakfastFragment() }
            1-> { LunchFragment() }
            2-> { DinnerFragment() }
            else -> { BreakfastFragment() }
        }
    }
}