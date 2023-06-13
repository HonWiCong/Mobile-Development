package com.example.shopnowseller.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.shopnowseller.R
import com.example.shopnowseller.data_class.Order
import androidx.appcompat.widget.Toolbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class OrderDetailActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar
    private val database = Firebase.firestore

    private lateinit var productName : TextView
    private lateinit var total : TextView
    private lateinit var quantity : TextView
    private lateinit var dateText : TextView
    private lateinit var address : TextView
    private lateinit var buyerName : TextView
    private lateinit var payment : TextView
    private lateinit var actionButton : Button
    private lateinit var cancelButton : Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Order detail"

        val order : Order = intent.getParcelableExtra("order")!!
        val type = intent.getStringExtra("type")
        productName = findViewById(R.id.order_detail_product_name)
        total = findViewById(R.id.order_detail_total)
        quantity = findViewById(R.id.order_detail_quantity)
        dateText = findViewById(R.id.order_detail_date)
        address = findViewById(R.id.order_detail_address)
        buyerName = findViewById(R.id.order_detail_buyer_name)
        payment = findViewById(R.id.order_detail_payment)
        actionButton = findViewById(R.id.order_detail_action_button)
        cancelButton = findViewById(R.id.order_detail_cancel_order_button)

        val firebaseTimestamp: Timestamp = order.date!!
        val date: Date = firebaseTimestamp.toDate()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault())
        val formattedDate: String = dateFormat.format(date)

        productName.text = "Product name: " + order.product_name
        total.text = "Total: RM" + order.totalPrice.toString()
        quantity.text = "Qty: " + order.quantity
        dateText.text = "Date/Time: " + formattedDate
        address.text = "Address: " + order.address
        buyerName.text = "Buyer: " + order.buyer_name
        payment.text = "Payment: " + order.payment

        if (type == "To Ship") {
            actionButton.text = "Shipped"
            actionButton.setOnClickListener {
                AlertDialog.Builder(this).apply {
                    setTitle("Change shipping status")
                    setMessage("Are you sure you have shipped the product?")
                    setPositiveButton("Yes") { _, _ ->
                        database
                            .collection("orders")
                            .document(order.id)
                            .update("status", "To Receive")
                            .addOnSuccessListener {
                                Toast.makeText(context, "The shipping status has been updated.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra("navigation_tab", "Order")
                                intent.putExtra("tab_layout_tab", 1)
                                startActivity(intent)
                            }
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }

            }
            cancelButton.setOnClickListener {
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
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra("navigation_tab", "Order")
                                intent.putExtra("tab_layout_tab", 3)
                                startActivity(intent)
                            }
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
            }
        } else {
            actionButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
        }

    }


}