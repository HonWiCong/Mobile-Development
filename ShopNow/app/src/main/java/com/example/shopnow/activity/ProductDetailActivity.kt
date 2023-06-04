package com.example.shopnow.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.shopnow.MainActivity
import com.example.shopnow.R
import com.example.shopnow.data_class.Account
import com.example.shopnow.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Color

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
        val quantity = findViewById<TextView>(R.id.detail_product_quantity)
        val description = findViewById<TextView>(R.id.detail_product_description)
        val shopThumbnail = findViewById<ImageView>(R.id.detail_shop_thumbnail)
        val cartButton = findViewById<Button>(R.id.detail_add_to_cart_button)
        val goToCartButton = findViewById<ImageView>(R.id.detail_cart)
        val buyNowButton = findViewById<Button>(R.id.buy_now_button)

        name.text = product.name

        Glide.with(this)
            .load(product.image)
            .into(image)

        Glide.with(this)
            .load(product.seller_thumbnail)
            .into(shopThumbnail)

        price.text = product.price.toString()
        rating.text = "Rate: ${product.rating}"
        quantity.text = product.quantity.toString() + " left"
        description.text = product.description

        cartButton.setOnClickListener {
            addProductToCart(product)
        }

        goToCartButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragmentToLoad", "cartFragment")
            startActivity(intent)
        }

        buyNowButton.setOnClickListener {
            showBottomDialog(product)
        }
    }

    private fun addProductToCart(product: Product) {
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
                            .update(updatedAccount as Map<String, Any>) // Use update() instead of set()
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

    private fun showBottomDialog(product: Product) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)

        val image = dialog.findViewById<ImageView>(R.id.bottom_sheet_product_image)
        val price = dialog.findViewById<TextView>(R.id.bottom_sheet_price)
        val minus = dialog.findViewById<ImageView>(R.id.bottom_sheet_minus_count)
        val add = dialog.findViewById<ImageView>(R.id.bottom_sheet_add_count)
        val quantity = dialog.findViewById<TextView>(R.id.bottom_sheet_quantity)
        val buyNowButton = dialog.findViewById<Button>(R.id.bottom_sheet_buy_now_button)
        var currentQuantity = 1

        Glide.with(this)
            .load(product.image)
            .into(image)

        price.text = product.price.toString()

        minus.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                quantity.text = currentQuantity.toString()
            }
        }

        add.setOnClickListener {
            if (currentQuantity < product.quantity!!) {
                currentQuantity++
                quantity.text = currentQuantity.toString()
            }
        }

        buyNowButton.setOnClickListener {
            val intent = Intent(this, PurchaseActivity::class.java)
            intent.putExtra("product", product)
            intent.putExtra("quantity", currentQuantity)
            startActivity(intent)
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

}