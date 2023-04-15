package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.title = "Co-Working Space Access Pass"
        init()
    }

    private lateinit var colabsCoworking: ConstraintLayout
    private lateinit var colonyCoworkingSpace: ConstraintLayout
    private lateinit var commonGround: ConstraintLayout
    private lateinit var headSpace: ConstraintLayout
    private lateinit var hourLoft: ConstraintLayout
    private lateinit var komuneLiving: ConstraintLayout
    private lateinit var title: TextView
    private var count = 0

    private fun init() {
        colabsCoworking = findViewById(R.id.colabs_coworking)
        colonyCoworkingSpace = findViewById(R.id.colony_coworking_space)
        commonGround = findViewById(R.id.common_ground)
        headSpace = findViewById(R.id.head_space)
        hourLoft = findViewById(R.id.hour_loft)
        komuneLiving = findViewById(R.id.komune_living)

        colabsCoworking.setOnClickListener{
            redirectToBookingPage(1)
        }
    }

    private fun redirectToBookingPage(id : Int) {
        val intent = Intent(this, booking_page::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

}