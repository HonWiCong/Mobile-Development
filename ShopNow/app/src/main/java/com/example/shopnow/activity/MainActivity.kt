package com.example.shopnow

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.shopnow.activity.LoginActivity
import com.example.shopnow.fragment.AccountFragment
import com.example.shopnow.fragment.CartFragment
import com.example.shopnow.fragment.HomeFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val PERMISSION_REQUEST_CODE = 112

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

//        if (Build.VERSION.SDK_INT > 32) {
//            if (!shouldShowRequestPermissionRationale("112")){
//                getNotificationPermission();
//            }
//        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new Instance ID token
            val token = task.result
            Log.e("NEW_TOKEN", token)
        })


        checkSignIn()
        fetchFCMToken()

        val fragmentToLoad = intent.getStringExtra("fragmentToLoad")

        if (fragmentToLoad == "cartFragment") {
            loadFragment(CartFragment())
        } else {
            loadFragment(HomeFragment())
        }

        navigationAction()
    }

//    fun getNotificationPermission() {
//        try {
//            if (Build.VERSION.SDK_INT > 32) {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                    PERMISSION_REQUEST_CODE
//                )
//            }
//        } catch (e: Exception) {
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow
                    Log.d("body", "granted")
                } else {
                    //deny
                    Log.d("permission","denied")
                }
                return
            }
        }
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

    private fun fetchFCMToken() {
        val uid = firebaseAuth.currentUser?.uid
        val documentRef = db.document("accounts/$uid")

        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.getString("token") == null) {
                // Token not present, generate and save it
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get new Instance ID token
                        val newToken = task.result
                        // Save it in the database
                        documentRef.update("token", newToken)
                    }
                }
            }
        }
    }
}
