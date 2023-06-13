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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.login_email_input)
        val password = findViewById<EditText>(R.id.login_password_input)
        val loginButton = findViewById<Button>(R.id.login_button)
        val redirectText = findViewById<TextView>(R.id.login_redirect_text)


        redirectText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            if (email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Fetch or generate a new FCM token if it doesn't exist
                        fetchFCMToken()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Email or password is incorrect", Toast.LENGTH_SHORT).show()
            }
        }
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
