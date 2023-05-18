package com.example.shopnow.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shopnow.fragment.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomepageViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)  : FragmentStateAdapter(fragmentManager, lifecycle)  {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        val db = Firebase.firestore

        return when(position) {
            0-> { AllProductFragment() }
            1-> { FoodFragment() }
            2-> { TechnologyFragment() }
            3-> { DailyFragment() }
            else -> { AllProductFragment() }
        }

    }
}
