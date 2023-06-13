package com.example.shopnow.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activity.LoginActivity
import com.example.shopnow.activity.MyPurchaseActivity
import com.example.shopnow.activity.UserEditActivity
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
        val profileImage = view.findViewById<ImageView>(R.id.account_image)
        val email = view.findViewById<TextView>(R.id.email_address_text)
        val toShip = view.findViewById<LinearLayout>(R.id.to_ship)
        val toReceive = view.findViewById<LinearLayout>(R.id.to_receive)
        val completed = view.findViewById<LinearLayout>(R.id.completed)
        val cancelled = view.findViewById<LinearLayout>(R.id.cancelled)
        val purchaseHistory = view.findViewById<TextView>(R.id.purchase_history_text)
        val edit = view.findViewById<ImageView>(R.id.acount_edit)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val accountsCollection = db.collection("accounts")

        var name = ""
        var image = ""
        var id = currentUser!!.uid

        accountsCollection
            .document(currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                name = it.getString("username").toString()
                image = it.getString("image").toString()
                username.text = name

                Glide.with(this)
                    .load(image)
                    .into(profileImage)
            }

        email.text = currentUser.email.toString()

        signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        toShip.setOnClickListener {
            val intent = Intent(context, MyPurchaseActivity::class.java)
            intent.putExtra("TAB_POSITION", 0)
            startActivity(intent)
        }

        toReceive.setOnClickListener {
            val intent = Intent(context, MyPurchaseActivity::class.java)
            intent.putExtra("TAB_POSITION", 1)
            startActivity(intent)
        }

        completed.setOnClickListener {
            val intent = Intent(context, MyPurchaseActivity::class.java)
            intent.putExtra("TAB_POSITION", 2)
            startActivity(intent)
        }

        cancelled.setOnClickListener {
            val intent = Intent(context, MyPurchaseActivity::class.java)
            intent.putExtra("TAB_POSITION", 3)
            startActivity(intent)
        }

        purchaseHistory.setOnClickListener {
            val intent = Intent(context, MyPurchaseActivity::class.java)
            intent.putExtra("TAB_POSITION", 2)
            startActivity(intent)
        }

        edit.setOnClickListener {
            val intent = Intent(context, UserEditActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        return view
    }

}