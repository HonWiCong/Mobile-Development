package com.example.assignment2task2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class orderComplete : Fragment() {
    private lateinit var selectedProductList: ArrayList<Product>
    var tipValue = 0.0
    var discount = 0.0
    var totalValue = 0.0

    private lateinit var discountValueTextView: TextView
    private lateinit var tipValueTextView: TextView
    private lateinit var totalValueTextView: TextView
    private lateinit var tipAmountTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_order_complete, container, false)
        discountValueTextView = view?.findViewById<TextView>(R.id.discount_value)!!
        tipValueTextView = view?.findViewById<TextView>(R.id.tip_value)!!
        totalValueTextView = view?.findViewById<TextView>(R.id.total_value)!!
        tipAmountTextView = view?.findViewById<TextView>(R.id.tip_amount)!!

        // button
        val zeroButton = view?.findViewById<Button>(R.id.zero_percent_button)
        val fiveButton = view?.findViewById<Button>(R.id.five_percent_button)
        val tenButton = view?.findViewById<Button>(R.id.ten_percent_button)
        val fifteenButton = view?.findViewById<Button>(R.id.fifteen_percent_button)
        val twentyButton = view?.findViewById<Button>(R.id.twenty_percent_button)
        val clearButton = view?.findViewById<Button>(R.id.clear_button)

        // Attach on click listener to button
        zeroButton?.setOnClickListener{ giveTip(0.0) }
        fiveButton?.setOnClickListener{ giveTip(0.05) }
        tenButton?.setOnClickListener{ giveTip(0.1) }
        fifteenButton?.setOnClickListener{ giveTip(0.15) }
        twentyButton?.setOnClickListener{ giveTip(0.2) }
        val onClickListener = clearButton?.setOnClickListener { clear() }

        selectedProductList = arguments?.getParcelableArrayList<Product>("selectedProductList") ?: arrayListOf()
        val selectedProductNameList = ArrayList<String>()
        val selectedProductPriceList = ArrayList<String>()
        for (selectedProduct in selectedProductList) {
            selectedProductNameList.add(selectedProduct.name.toString())
            selectedProductPriceList.add("RM " + selectedProduct.price.toString())
        }

        val productNameListTextView = view.findViewById<TextView>(R.id.product_name_list)
        val productPriceListTextView = view.findViewById<TextView>(R.id.product_price_list)
        productNameListTextView?.text = selectedProductNameList.joinToString(separator = "\n")
        productPriceListTextView?.text = selectedProductPriceList.joinToString(separator = "\n")
        calculatePrice()

        return view
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

        for (product in selectedProductList) {
            if (product.name?.contains("Salmon") == true ||
                product.name?.contains("Smoked Duck") == true ||
                product.name?.contains("Turkey") == true ||
                product.name?.contains("Cheese") == true
            ) {
                totalValue += (product.price * 0.9)
                discount += (product.price * 0.1)
            }
            else {
                totalValue += product.price
            }
        }

        discountValueTextView.text = "RM $discount"
        tipValueTextView.text = "RM $tipValue"
        totalValueTextView.text = "RM ${totalValue + tipValue}"
    }

    private fun clear() {
        val menuFragment = requireActivity().supportFragmentManager.findFragmentByTag("menu") as menu?

        val menu = menu()
        fragmentManager?.beginTransaction()?.replace(R.id.main_screen_fragment, menu)?.commit()

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.main_screen_fragment, menu)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}