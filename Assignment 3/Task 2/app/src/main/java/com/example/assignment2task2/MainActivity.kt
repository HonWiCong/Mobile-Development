package com.example.assignment2task2

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), Communicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Heath Living"
        val colorDrawable = ColorDrawable(Color.parseColor("#006400"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        val menuFragment = menu()
        supportFragmentManager.beginTransaction().replace(R.id.main_screen_fragment, menuFragment).commit()
    }

    override fun selectedProductPassing(selectedProductList: ArrayList<Product>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("selectedProductList", selectedProductList)

        val transaction = this.supportFragmentManager.beginTransaction()
        val orderComplete = orderComplete()
        orderComplete.arguments = bundle

        transaction.replace(R.id.main_screen_fragment, orderComplete).commit()
    }

}