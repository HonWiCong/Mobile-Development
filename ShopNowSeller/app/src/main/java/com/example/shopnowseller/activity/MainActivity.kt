package com.example.shopnowseller.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.shopnowseller.R
import com.example.shopnowseller.fragment.ShopFragment
import com.example.shopnowseller.fragment.ProductFragment
import com.example.shopnowseller.fragment.OrderFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = firebaseAuth.currentUser
    private lateinit var navigation: BottomNavigationView
    private var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // firebaseAuth.signOut()
        checkSignIn()

        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar2)
        setSupportActionBar(toolbar)

        val logoutImage = findViewById<ImageView>(R.id.logout_button)
        logoutImage.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        navigation = findViewById(R.id.bottomNavigationView)
        navigationAction()

        val navigationTab: String? = intent.getStringExtra("navigation_tab")
        if (navigationTab != null && navigationTab == "Order") {
            navigation.selectedItemId = R.id.bottom_navigation_order
            navigation.menu.performIdentifierAction(R.id.bottom_navigation_order, 0)
        } else {
            loadFragment(ProductFragment())
        }
    }

    override fun onResume() {
        super.onResume()

        // Moved Firestore retrieval here
        database
            .collection("shops")
            .document(currentUser!!.uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d("MainActivity", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    supportActionBar?.title = snapshot.getString("name").toString()
                    Log.d("MainActivity", snapshot.getString("name").toString())
                } else {
                    Log.d("MainActivity", "Current data: null")
                }
            }

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
                    loadFragment(ProductFragment())
                }

                R.id.bottom_navigation_shop -> {
                    loadFragment(ShopFragment())
                }

                R.id.bottom_navigation_order -> {
                    loadFragment(OrderFragment())
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