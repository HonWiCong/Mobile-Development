package com.example.shopnow.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.shopnow.MainActivity
import com.example.shopnow.R
import com.example.shopnow.data_class.Account
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserEditActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private var imageUri : Uri? = null
    private lateinit var image : ImageView
    private var storageRef = FirebaseStorage.getInstance()
    private var imageDownloadURL = ""
    private lateinit var galleryImage: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val userImage = intent.getStringExtra("image")
        val username = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")



        val name = findViewById<EditText>(R.id.edit_name_input)
        image = findViewById(R.id.edit_image)
        val uploadImageButton = findViewById<Button>(R.id.edit_upload_image_button)
        val submitButton = findViewById<Button>(R.id.edit_submit_button)

        name.text = Editable.Factory.getInstance().newEditable(username)


        if (userImage != "") {
            Glide.with(this)
                .load(userImage)
                .into(image)
        }

        galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { result ->
                image.setImageURI(result)
                imageUri = result!!
            }
        )

        image.setOnClickListener {
            galleryImage.launch("image/*")
        }

        uploadImageButton.setOnClickListener {
            storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(imageUri!!)
                .addOnCompleteListener {
                    it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        imageDownloadURL = it.toString()
                    }
                }

        }

        submitButton.setOnClickListener {
            val database = FirebaseFirestore.getInstance()

            val editedAccount = Account(
                username = name.text.toString(),
                image = imageDownloadURL
            )

            val accountMap = mapOf(
                "username" to editedAccount.username,
                "image" to editedAccount.image,
            )

            database
                .collection("accounts")
                .document(id!!)
                .update(accountMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Account updated successfully",  Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { Toast.makeText(this, "Failed to update account",  Toast.LENGTH_SHORT).show() }
        }
    }
}