package com.example.shopnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shopnow.fragment.AccountFragment
import com.example.shopnow.fragment.CartFragment
import com.example.shopnow.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.homepage_fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun navigationAction() {
        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigation.setOnNavigationItemReselectedListener {
            when(it.itemId) {
                R.id.bottom_navigation_home-> {
                    loadFragment(HomeFragment())
                }

                R.id.bottom_navigation_cart -> {
                    loadFragment(CartFragment())
                }

                R.id.bottom_navigation_account -> {
                    loadFragment(AccountFragment())
                }
            }
        }
    }
}