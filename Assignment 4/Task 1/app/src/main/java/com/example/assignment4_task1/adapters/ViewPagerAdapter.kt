package com.example.assignment4_task1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignment4_task1.fragments.ScienceFragment
import com.example.assignment4_task1.fragments.TechnologyFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> { ScienceFragment() }
            1-> { TechnologyFragment() }
            else-> { ScienceFragment() }
        }

    }

}