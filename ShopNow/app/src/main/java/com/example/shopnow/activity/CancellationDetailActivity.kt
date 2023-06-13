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

class CancellationDetailActivity : AppCompatActivity() {
    private val database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancellation_detail)

        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Cancellation Detail"

        val order : Order = intent.getParcelableExtra("order")!!

        val productImage = findViewById<ImageView>(R.id.cancellation_detail_product_image)
        val productName = findViewById<TextView>(R.id.cancellation_detail_product_name)
        val productPrice = findViewById<TextView>(R.id.cancellation_detail_product_price)
        val orderID = findViewById<TextView>(R.id.cancellation_detail_order_id)
        val deleteButton = findViewById<Button>(R.id.cancellation_detail_delete_order_button)

        Glide.with(this)
            .load(order.product_image)
            .into(productImage)

        productName.text = order.product_name
        productPrice.text = "RM" + order.product_price
        orderID.text = order.id

        deleteButton.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Delete Order")
                setMessage("Are you sure you want to remove this order?")
                setPositiveButton("Yes") { _, _ ->
                    database
                        .collection("orders")
                        .document(order.id)
                        .update("visibility", false)
                        .addOnSuccessListener {
                            Toast.makeText(context, "You have deleted the order.", Toast.LENGTH_SHORT).show()
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