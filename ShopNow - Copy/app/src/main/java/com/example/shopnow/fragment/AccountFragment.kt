package com.example.shopnow.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.shopnow.R
import com.example.shopnow.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("accounts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val signOut = view.findViewById<ImageView>(R.id.logout_image)
        val username = view.findViewById<TextView>(R.id.username_text)
        val email = view.findViewById<TextView>(R.id.email_address_text)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val accountsCollection = db.collection("accounts")
        accountsCollection
            .document(currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                username.text = it.getString("username").toString()
            }

        email.text = currentUser?.email.toString()

        signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        return view
    }

}