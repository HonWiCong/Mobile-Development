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

//import androidx.fragment.app.FragmentActivity
//
//
//private const val NUM_PAGES = 5
//
//class ScreenSlidePagerActivity : FragmentActivity() {
//
//    private lateinit var viewPager: ViewPager2
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_home)
//
//        // Instantiate a ViewPager2 and a PagerAdapter.
//        viewPager = findViewById(R.id.viewPager2)
//
//        // The pager adapter, which provides the pages to the view pager widget.
//        val pagerAdapter = HomepageViewPagerAdapter(this)
//        viewPager.adapter = pagerAdapter
//    }
//
//    override fun onBackPressed() {
//        if (viewPager.currentItem == 0) {
//            super.onBackPressed()
//        } else {
//            // Otherwise, select the previous step.
//            viewPager.currentItem = viewPager.currentItem - 1
//        }
//    }
//
//    private inner class HomepageViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
//        override fun getItemCount(): Int = NUM_PAGES
//
//        override fun createFragment(position: Int): Fragment = HomeFragment()
//    }
//}