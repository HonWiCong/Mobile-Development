package com.example.shopnow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shopnow.activity.LoginActivity
import com.example.shopnow.data_class.Account
import com.example.shopnow.data_class.Product
import com.example.shopnow.fragment.AccountFragment
import com.example.shopnow.fragment.CartFragment
import com.example.shopnow.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        firebaseAuth.signOut()
        checkSignIn()

        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")

        if (fragmentToLoad == "cartFragment") {
            loadFragment(CartFragment())
        } else {
            loadFragment(HomeFragment())
            // Load default fragment or handle other cases
        }

        navigationAction()

    }

    private fun checkSignIn() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homepage_fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}