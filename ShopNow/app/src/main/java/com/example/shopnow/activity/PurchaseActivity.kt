package com.example.shopnow.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.shopnow.MainActivity
import com.example.shopnow.R
import com.example.shopnow.data_class.Order
import com.example.shopnow.data_class.Product
import com.example.shopnow.data_class.Shop
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException


class PurchaseActivity : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseFirestore.getInstance()
    private val client = OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Checkout"

        val shopName = findViewById<TextView>(R.id.purchase_shop_name)
        val image = findViewById<ImageView>(R.id.purchase_product_image)
        val name = findViewById<TextView>(R.id.purchase_product_name)
        val price = findViewById<TextView>(R.id.purchase_price)
        val quantity = findViewById<TextView>(R.id.purchase_quantity)
        val subtotal = findViewById<TextView>(R.id.purchase_subtotal)
        val orderAmount = findViewById<TextView>(R.id.purchase_order_amount)
        val total = findViewById<TextView>(R.id.purchase_total)
        val orderButton = findViewById<TextView>(R.id.purchase_order_button)

        val product : Product = intent.getParcelableExtra("product")!!
        val orderQuantity = intent.getIntExtra("quantity", 1)

        Glide.with(this)
            .load(product.image)
            .into(image)

        name.text = product.name
        price.text = "RM" + product.price.toString()
        quantity.text = "x$orderQuantity"
        subtotal.text = "RM" + (product.price * orderQuantity).toString()
        orderAmount.text = "RM" + (product.price * orderQuantity + 10).toString()
        total.text = "RM" + (product.price * orderQuantity + 10).toString()

        var buyerName = ""

        database
            .collection("accounts")
            .document(currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    buyerName = it.result.getString("username").toString()
                }
            }

        database
            .collection("shops")
            .document(product.seller.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val shop = task.result?.toObject(Shop::class.java)
                    if (shop != null) {
                        shopName.text = shop.name
                    } else {
                        Toast.makeText(this, "Shop not found or incorrect format.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error getting shop: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

        orderButton.setOnClickListener {
            val current = Timestamp.now()

            val order = Order(
                product_id = product.id!!,
                quantity = orderQuantity,
                buyer_id = currentUser.uid,
                buyer_name = buyerName,
                seller_id = product.seller!!,
                totalPrice = product.price * orderQuantity + 10,
                date = current,
                delivery_fee = 10.00,
                payment = "Online Banking",
                product_name = product.name!!,
                product_image = product.image!!,
                product_price = product.price
            )

            database
                .collection("orders")
                .add(order)
                .addOnSuccessListener {
                    Toast.makeText(this, "Order placed", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            val deducted = mapOf(
                "quantity" to (product.quantity?.minus(orderQuantity)),
            )

            database
                .collection("products")
                .document(product.id!!)
                .update(deducted)

        }
    }

}