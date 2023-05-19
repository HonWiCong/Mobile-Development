package com.example.shopnow.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.data_class.Account
import com.example.shopnow.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailActivity : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    private val accountsCollection = db.collection("accounts")

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val product : Product = intent.getParcelableExtra("product")!!

        val image = findViewById<ImageView>(R.id.detail_product_image)
        val name = findViewById<TextView>(R.id.detail_product_name)
        val price = findViewById<TextView>(R.id.detail_product_price)
        val rating = findViewById<TextView>(R.id.detail_product_rating)
        val description = findViewById<TextView>(R.id.detail_product_description)
        val shopThumbnail = findViewById<ImageView>(R.id.detail_shop_thumbnail)
        val cartButton = findViewById<Button>(R.id.detail_add_to_cart_button)

        name.text = product.name

        Glide.with(this)
            .load(product.image)
            .into(image);

        Glide.with(this)
            .load(product.seller_thumbnail)
            .into(shopThumbnail);

        price.text = product.price.toString()
        rating.text = "Rate: ${product.rating}"
        description.text = product.description

        cartButton.setOnClickListener {
            addProductToCart(product)
        }
    }

    fun addProductToCart(product: Product) {
        val accountDocumentRef = accountsCollection.document(currentUser!!.uid)

        accountDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val account = documentSnapshot.toObject(Account::class.java)

                    if (account != null) {
                        val cartList = account.cart_list ?: ArrayList()
                        cartList.add(product.id.toString())

                        val updatedAccount = hashMapOf(
                            "cart_list" to cartList
                        )

                        accountDocumentRef
                            .set(updatedAccount)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                println("Error adding product to cart: $e")
                            }
                    }
                } else {
                    println("User document does not exist")
                }
            }
            .addOnFailureListener { e ->
                println("Error retrieving user document: $e")
            }

    }
}