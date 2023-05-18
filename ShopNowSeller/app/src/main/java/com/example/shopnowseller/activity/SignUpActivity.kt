package com.example.shopnowseller.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopnowseller.R
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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
            if (email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() && passwordConfirm.text.toString().isNotEmpty()) {
                if (password.text.toString() == passwordConfirm.text.toString()) {
                    firebaseAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
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
}