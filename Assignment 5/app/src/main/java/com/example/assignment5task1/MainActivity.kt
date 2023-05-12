package com.example.assignment5task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.example.assignment5task1.fragments.CompletedFragment
import com.example.assignment5task1.fragments.HomeFragment
import com.example.assignment5task1.fragments.ProgressFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.home-> {
                    loadFragment(HomeFragment())
                }

                R.id.progress -> {
                    loadFragment(ProgressFragment())
                }

                R.id.completed -> {
                    loadFragment(CompletedFragment())
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}