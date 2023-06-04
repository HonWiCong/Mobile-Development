package com.example.shopnowseller.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.shopnowseller.R
import com.example.shopnowseller.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import androidx.appcompat.widget.Toolbar


class EditActivity : AppCompatActivity() {
    private var imageUri : Uri? = null
    private lateinit var image : ImageView
    private var storageRef = FirebaseStorage.getInstance()
    private var imageDownloadURL = ""
    private lateinit var galleryImage: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val product : Product = intent.getParcelableExtra("product")!!

        val name = findViewById<EditText>(R.id.edit_product_name_input)
        val price = findViewById<EditText>(R.id.edit_price_input)
        val description = findViewById<EditText>(R.id.edit_description_input)
        val quantity = findViewById<EditText>(R.id.edit_quantity_input)
        val category = findViewById<Spinner>(R.id.edit_category_spinner)
        image = findViewById(R.id.edit_image)
        val uploadImageButton = findViewById<Button>(R.id.edit_upload_image_button)
        val activeRadio = findViewById<RadioButton>(R.id.edit_active_radio)
        val inactiveRadio = findViewById<RadioButton>(R.id.edit_inactive_radio)
        val submitButton = findViewById<Button>(R.id.edit_submit_button)
        val deleteButton = findViewById<Button>(R.id.edit_delete_button)

        name.text = Editable.Factory.getInstance().newEditable(product.name)
        price.text = Editable.Factory.getInstance().newEditable(product.price.toString())
        description.text = Editable.Factory.getInstance().newEditable(product.description)
        quantity.text = Editable.Factory.getInstance().newEditable(product.quantity.toString())

        Glide.with(this)
            .load(product.image)
            .into(image)

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

        val database = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        val categoryOptions = resources.getStringArray(R.array.category)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryOptions)
        category.adapter = spinnerAdapter

        var status: Boolean
        if (product.status == true) {
            activeRadio.isChecked = true
            status = true
        } else {
            inactiveRadio.isChecked = true
            status = false
        }

        activeRadio.setOnClickListener {
            if (activeRadio.isChecked) status = true
        }

        inactiveRadio.setOnClickListener {
            if (inactiveRadio.isChecked) status = false
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

        submitButton.setOnClickListener {
            if (imageDownloadURL == "") imageDownloadURL = product.image!!

            val editedProduct = Product(
                id = product.id,
                name = name.text.toString(),
                price = price.text.toString().toDouble(),
                quantity = quantity.text.toString().toInt(),
                image = imageDownloadURL,
                description = description.text.toString(),
                seller = currentUser!!.uid,
                category = categorySelected,
                status = status
            )

            val productMap = mapOf(
                "id" to editedProduct.id,
                "name" to editedProduct.name,
                "price" to editedProduct.price,
                "quantity" to editedProduct.quantity,
                "image" to editedProduct.image,
                "description" to editedProduct.description,
                "seller" to editedProduct.seller,
                "category" to editedProduct.category,
                "status" to editedProduct.status
            )

            database
                .collection("products")
                .document(product.id.toString())
                .update(productMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Product edited successfully",  Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { Toast.makeText(this, "Failed to edit product",  Toast.LENGTH_SHORT).show() }
        }

        deleteButton.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirm Delete")
            alertDialogBuilder.setMessage("Are you sure you want to delete this item?")
            alertDialogBuilder.setPositiveButton("Delete") { dialog, _ ->
                // Delete the item
                database
                    .collection("products")
                    .document(product.id.toString())
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show()
                    }
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

}