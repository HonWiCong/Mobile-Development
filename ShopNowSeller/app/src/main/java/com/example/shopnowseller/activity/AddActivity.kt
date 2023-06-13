package com.example.shopnowseller.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shopnowseller.R
import com.example.shopnowseller.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AddActivity : AppCompatActivity() {
    private var imageUri : Uri? = null
    private lateinit var image : ImageView
    private var storageRef = FirebaseStorage.getInstance()
    private var imageDownloadURL = ""
    private lateinit var galleryImage: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Create Product"

        val name = findViewById<EditText>(R.id.add_product_name_input)
        val price = findViewById<EditText>(R.id.add_price_input)
        val description = findViewById<EditText>(R.id.add_description_input)
        val category = findViewById<Spinner>(R.id.add_category_spinner)
        val quantity = findViewById<EditText>(R.id.add_quantity_input)
        image = findViewById(R.id.add_image)
        val uploadImageButton = findViewById<Button>(R.id.add_upload_image_button)
        val activeRadio = findViewById<RadioButton>(R.id.add_active_radio)
        val inactiveRadio = findViewById<RadioButton>(R.id.add_inactive_radio)
        val submitButton = findViewById<Button>(R.id.add_submit_button)

        val database = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        val categoryOptions = resources.getStringArray(R.array.category)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryOptions)
        category.adapter = spinnerAdapter

        var status = true
        activeRadio.setOnClickListener {
            if (activeRadio.isChecked) status = true
        }

        inactiveRadio.setOnClickListener {
            if (activeRadio.isChecked) status = false
        }

        var categorySelected = ""
        category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> { categorySelected = "Technology" }
                    1 -> { categorySelected = "Daily" }
                    2 -> { categorySelected = "Food" }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                categorySelected = "Daily"
            }
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
            if (name.text.isNullOrEmpty() || price.text.isNullOrEmpty() || imageDownloadURL == "" || quantity.text.isNullOrEmpty() || description.text.isNullOrEmpty() || categorySelected == "") {
                Toast.makeText(this, "All field are required!",  Toast.LENGTH_SHORT).show()
            } else {
                val product = Product(
                    name = name.text.toString(),
                    price = price.text.toString().toDouble(),
                    image = imageDownloadURL,
                    quantity = quantity.text.toString().toInt(),
                    description = description.text.toString(),
                    seller = currentUser!!.uid,
                    category = categorySelected,
                    status = status
                )

                database
                    .collection("products")
                    .add(product)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Product added successfully",  Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { Toast.makeText(this, "Failed to add product",  Toast.LENGTH_SHORT).show() }
            }
        }
    }
}