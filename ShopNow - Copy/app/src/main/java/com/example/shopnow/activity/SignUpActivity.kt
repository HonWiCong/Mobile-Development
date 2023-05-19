package com.example.shopnow.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.shopnow.MainActivity
import com.example.shopnow.R
import com.example.shopnow.data_class.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var username : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        username = findViewById(R.id.sign_up_username_input)
        val email = findViewById<EditText>(R.id.sign_up_email_input)
        val password = findViewById<EditText>(R.id.sign_up_password_input)
        val passwordConfirm = findViewById<EditText>(R.id.sign_up_confirm_password_input)
        val redirectText = findViewById<TextView>(R.id.sign_up_redicrect_text)
        val signUpButton = findViewById<Button>(R.id.sign_up_button)

        redirectText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            if (username.text.toString().isNotEmpty() && email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() && passwordConfirm.text.toString().isNotEmpty()) {
                if (password.text.toString() == passwordConfirm.text.toString()) {
                    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            createUser()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All field are required!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val accountsCollection = db.collection("accounts")
        val accountDocument = currentUser?.let { accountsCollection.document(it.uid) }

        val account = Account(
            username = username.text.toString(),
            email = currentUser?.email.toString(),
            address = "",
            image = "",
            cart_list = ArrayList<String>()
        )

        if (accountDocument != null) {
            accountDocument
                .set(account)
                .addOnSuccessListener {
                    println("Account created successfully")
                }
                .addOnFailureListener { e ->
                    println("Error creating account: $e")
                }
        }
    }
}