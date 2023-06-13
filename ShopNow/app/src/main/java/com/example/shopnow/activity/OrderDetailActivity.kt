package com.example.shopnow.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.data_class.Order
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderDetailActivity : AppCompatActivity() {
    private val database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Order Detail"

        val address = findViewById<TextView>(R.id.order_detail_address)
        val productImage = findViewById<ImageView>(R.id.order_detail_product_image)
        val productName = findViewById<TextView>(R.id.order_detail_product_name)
        val productQuantity = findViewById<TextView>(R.id.order_detail_product_quantity)
        val productPrice = findViewById<TextView>(R.id.order_detail_product_price)
        val subtotal = findViewById<TextView>(R.id.order_detail_subtotal)
        val shippingFee = findViewById<TextView>(R.id.order_detail_shipping_fee)
        val total = findViewById<TextView>(R.id.order_detail_total)
        val cancelOrder = findViewById<Button>(R.id.order_detail_cancel_order_button)

        val order : Order = intent.getParcelableExtra("order")!!

        address.text = order.address

        Glide.with(this)
            .load(order.product_image)
            .into(productImage)

        productName.text = order.product_name
        productQuantity.text = "x" + order.quantity
        productPrice.text = "RM" + order.product_price
        subtotal.text = "RM" + (order.quantity * order.product_price)
        shippingFee.text = "RM" + order.delivery_fee
        total.text = "RM" + order.totalPrice

        cancelOrder.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Cancel Order")
                setMessage("Are you sure you want to cancel this order?")
                setPositiveButton("Yes") { _, _ ->
                    database
                        .collection("orders")
                        .document(order.id)
                        .update("status", "Cancelled")
                        .addOnSuccessListener {
                            Toast.makeText(context, "You have cancelled the order.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, MyPurchaseActivity::class.java)
                            intent.putExtra("TAB_POSITION", 3)
                            startActivity(intent)
                        }
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }
    }
}