package com.example.assignment5task1

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.appbar.MaterialToolbar

class ViewActivity : AppCompatActivity() {
    private lateinit var taskText : TextView
    private lateinit var descriptionText : TextView
    private lateinit var dateText: TextView
    private lateinit var priorityText : TextView
    private lateinit var priorityColor : CardView
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        val materialToolbar: MaterialToolbar = findViewById(R.id.materialToolbar)

        taskText = findViewById(R.id.edit_task_text)
        descriptionText = findViewById(R.id.view_description_text)
        dateText = findViewById(R.id.view_date_text)
        priorityText = findViewById(R.id.view_priority_text)
        priorityColor = findViewById(R.id.view_priototy_color)

        id = intent.getStringExtra("id").toString()
        taskText.text = intent.getStringExtra("task")
        descriptionText.text = intent.getStringExtra("description")
        dateText.text = intent.getStringExtra("date")
        priorityText.text = intent.getStringExtra("priority")

        when (priorityText.text) {
            "low" -> { priorityColor.setCardBackgroundColor(Color.parseColor("#46892a")) }
            "medium" -> { priorityColor.setCardBackgroundColor(Color.parseColor("#e98930")) }
            "high" -> { priorityColor.setCardBackgroundColor(Color.parseColor("#bf2a05")) }
        }

        // assign the on menu item click listener
        materialToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(this, EditActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("task", taskText.text.toString())
                    intent.putExtra("description", descriptionText.text.toString())
                    intent.putExtra("date", dateText.text.toString())
                    intent.putExtra("priority", priorityText.text.toString())
                    intent.putExtra("status", intent.getStringExtra("status"))
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }



    }
}