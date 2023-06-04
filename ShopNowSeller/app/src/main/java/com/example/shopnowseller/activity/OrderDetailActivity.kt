package com.example.shopnowseller.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.shopnowseller.R
import com.example.shopnowseller.data_class.Order
import androidx.appcompat.widget.Toolbar


class OrderDetailActivity : AppCompatActivity() {
    private lateinit var toolbar : Toolbar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        toolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val order : Order = intent.getParcelableExtra("order")!!
        val productName = findViewById<TextView>(R.id.order_detail_product_name)
        val total = findViewById<TextView>(R.id.order_detail_total)
        val quantity = findViewById<TextView>(R.id.order_detail_quantity)
        val date = findViewById<TextView>(R.id.order_detail_date)
        val address = findViewById<TextView>(R.id.order_detail_address)
        val buyerName = findViewById<TextView>(R.id.order_detail_buyer_name)
        val payment = findViewById<TextView>(R.id.order_detail_payment)

        productName.text = "Product name: " + order.product_name
        total.text = "Total: RM" + order.totalPrice.toString()
        quantity.text = "Qty: " + order.quantity
        date.text = "Date/Time: " + order.date
        address.text = "Address: " + order.address
        buyerName.text = "Buyer: " + order.buyer_name
        payment.text = "Payment: " + order.payment
    }
}