package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class order_complete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_complete)
        supportActionBar?.title = "Co-Working Space Access Pass"
        init()
    }

    private var totalPrice = 0
    private var type = ""
    private var resourceId = 0
    private var name = ""
    private var quantity = 0


    private lateinit var cover: ImageView
    private lateinit var nameText: TextView
    private lateinit var typeText: TextView
    private lateinit var quantityText: TextView
    private lateinit var validityText: TextView




    @SuppressLint("SetTextI18n")
    private fun init() {
        totalPrice = intent.getIntExtra("totalPrice", 0)
        type = intent.getStringExtra("type")!!
        resourceId = intent.getIntExtra("resourceId", 0)
        name = intent.getStringExtra("name")!!
        quantity = intent.getIntExtra("quantity", 1)

        cover = findViewById(R.id.order_complete_cover)
        nameText = findViewById(R.id.order_complete_name)
        typeText = findViewById(R.id.order_complete_type)
        quantityText = findViewById(R.id.order_complete_quantity)
        validityText = findViewById(R.id.order_complete_validity)

        cover.setImageResource(resourceId)
        nameText.text = name
        typeText.text = "Type: $type - RM $totalPrice"
        quantityText.text = "Quantity: $quantity"

        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH)
        var month = calendar.get(Calendar.MONTH) + 1
        var year = calendar.get(Calendar.YEAR)

        val currentDate = "$day/$month/$year"


        if (type.contains("Daily")) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            month = calendar.get(Calendar.MONTH) + 1
        } else if (type.contains("Weekly")){
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            month = calendar.get(Calendar.MONTH) + 1
        } else if (type.contains("Monthly")) {
            calendar.add(Calendar.MONTH, 2)
            month = calendar.get(Calendar.MONTH)
        }

        day = calendar.get(Calendar.DAY_OF_MONTH)
        year = calendar.get(Calendar.YEAR)
        var expiredDate = "$day/$month/$year"

        validityText.text = "Validity: $currentDate - $expiredDate"
    }
}