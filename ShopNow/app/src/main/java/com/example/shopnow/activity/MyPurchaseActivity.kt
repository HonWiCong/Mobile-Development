package com.example.shopnow.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.shopnow.R
import com.example.shopnow.adapter.HomepageViewPagerAdapter
import com.example.shopnow.adapter.MyPurchaseViewpagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPurchaseActivity : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseFirestore.getInstance()
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_purchase)

        toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "My Purchase"

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2)
        val viewPagerAdapter = MyPurchaseViewpagerAdapter(this)
        viewPager2.adapter = viewPagerAdapter

        val tabPosition = intent.getIntExtra("TAB_POSITION", 0)
        viewPager2.currentItem = tabPosition

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = viewPagerAdapter.getTabTitle(position)
        }.attach()
    }
}