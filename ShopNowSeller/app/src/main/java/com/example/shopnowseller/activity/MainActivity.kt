package com.example.shopnowseller.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shopnowseller.R
import com.example.shopnowseller.fragment.AddFragment
import com.example.shopnowseller.fragment.AllFragment
import com.example.shopnowseller.fragment.InactiveFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        firebaseAuth.signOut()

        loadFragment(AllFragment())
        checkSignIn()
        navigationAction()
    }

    private fun checkSignIn() {
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigationAction() {
        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigation.setOnNavigationItemReselectedListener {
            when(it.itemId) {
                R.id.bottom_navigation_all-> {
                    loadFragment(AllFragment())
                }

                R.id.bottom_navigation_add -> {
                    loadFragment(AddFragment())
                }

                R.id.bottom_navigation_unactive -> {
                    loadFragment(InactiveFragment())
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