package com.example.assignment2task2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class order : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        supportActionBar?.title = "Heath Living"
        val colorDrawable = ColorDrawable(Color.parseColor("#006400"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        init()
    }

    private lateinit var greenList: ArrayList<Product>
    private lateinit var proteinList: ArrayList<Product>
    private lateinit var sidesList: ArrayList<Product>
    private lateinit var dressingList: ArrayList<Product>
    private var selectedProductList = ArrayList<Product>()

    private lateinit var productNameListTextView: TextView
    private lateinit var productPriceListTextView: TextView
    private lateinit var discountValueTextView: TextView
    private lateinit var tipValueTextView: TextView
    private lateinit var totalValueTextView: TextView

    private lateinit var zeroButton: Button
    private lateinit var fiveButton: Button
    private lateinit var tenButton: Button
    private lateinit var fifteenButton: Button
    private lateinit var twentyButton: Button
    private lateinit var tipAmountTextView: TextView

    private lateinit var clearButton: Button

    var tipValue = 0.0
    var discount = 0.0
    var totalValue = 0.0

    private fun init() {
        // TextView
        discountValueTextView = findViewById(R.id.discount_value)
        tipValueTextView = findViewById(R.id.tip_value)
        totalValueTextView = findViewById(R.id.total_value)
        tipAmountTextView = findViewById(R.id.tip_amount)

        // button
        zeroButton = findViewById(R.id.zero_percent_button)
        fiveButton = findViewById(R.id.five_percent_button)
        tenButton = findViewById(R.id.ten_percent_button)
        fifteenButton = findViewById(R.id.fifteen_percent_button)
        twentyButton = findViewById(R.id.twenty_percent_button)
        clearButton = findViewById(R.id.clear_button)

        zeroButton.setOnClickListener{ giveTip(0.0) }
        fiveButton.setOnClickListener{ giveTip(0.05) }
        tenButton.setOnClickListener{ giveTip(0.1) }
        fifteenButton.setOnClickListener{ giveTip(0.15) }
        twentyButton.setOnClickListener{ giveTip(0.2) }
        clearButton.setOnClickListener{ clear() }

        // intent data
        greenList = intent.getSerializableExtra("green_list") as ArrayList<Product>
        proteinList = intent.getSerializableExtra("protein_list") as ArrayList<Product>
        sidesList = intent.getSerializableExtra("sides_list") as ArrayList<Product>
        dressingList = intent.getSerializableExtra("dressing_list") as ArrayList<Product>

        selectedProductList.addAll(greenList)
        selectedProductList.addAll(proteinList)
        selectedProductList.addAll(sidesList)
        selectedProductList.addAll(dressingList)

        val selectedProductNameList = ArrayList<String>()
        val selectedProductPriceList = ArrayList<String>()
        for (selectedProduct in selectedProductList) {
            selectedProductNameList.add(selectedProduct.name)
            selectedProductPriceList.add("RM " + selectedProduct.price.toString())
        }

        productNameListTextView = findViewById(R.id.product_name_list)
        productPriceListTextView = findViewById(R.id.product_price_list)
        productNameListTextView.text = selectedProductNameList.joinToString(separator = "\n")
        productPriceListTextView.text = selectedProductPriceList.joinToString(separator = "\n")
        calculatePrice()
    }

    @SuppressLint("SetTextI18n")
    private fun giveTip(percent : Double) {
        tipValue = totalValue * percent
        tipAmountTextView.text = "Tip Amount: RM $tipValue"
        calculatePrice()
    }

    @SuppressLint("SetTextI18n")
    private fun calculatePrice() {
        totalValue = 0.0
        discount = 0.0

        for (product in greenList) {
            totalValue += product.price
        }

        for (product in proteinList) {
            if (product.name.contains("Salmon") || product.name.contains("Smoked Duck") || product.name.contains("Turkey")) {
                totalValue += (product.price * 0.9)
                discount += (product.price * 0.1)
            }
            else {
                totalValue += product.price
            }
        }

        for (product in sidesList) {
            if (product.name.contains("Cheese")) {
                totalValue += (product.price * 0.9)
                discount += (product.price * 0.1)
            }
            else {
                totalValue += product.price
            }
        }

        for (product in dressingList) {
            totalValue += product.price
        }

        totalValue += tipValue
        discountValueTextView.text = "RM $discount"
        tipValueTextView.text = "RM $tipValue"
        totalValueTextView.text = "RM $totalValue"
    }

    private fun clear() {
        val intent = Intent()
        intent.putExtra("clear", true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}