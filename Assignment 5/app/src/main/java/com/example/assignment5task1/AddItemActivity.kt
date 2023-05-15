package com.example.assignment5task1

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AddItemActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var datePicker : MaterialCardView
    private lateinit var dateText : TextView
    private lateinit var submitButton : Button
    private lateinit var taskInput : EditText
    private lateinit var descriptionInput : EditText
    private lateinit var requiredText: TextView
    private lateinit var lowPriorityRadio : RadioButton
    private lateinit var mediumPriorityRadio : RadioButton
    private lateinit var highPriorityRadio : RadioButton

    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        datePicker = findViewById(R.id.date_picker)
        dateText = findViewById(R.id.date_text)
        submitButton = findViewById(R.id.submit_button)
        taskInput = findViewById(R.id.task_input)
        descriptionInput = findViewById(R.id.desciption_input)
        requiredText = findViewById(R.id.required_text)
        lowPriorityRadio = findViewById(R.id.low_priority_radio)
        mediumPriorityRadio = findViewById(R.id.medium_priority_radio)
        highPriorityRadio = findViewById(R.id.high_priority_radio)

        submitButton.setOnClickListener {
            var priority = ""
            if (lowPriorityRadio.isChecked) priority = "low"
            if (mediumPriorityRadio.isChecked) priority = "medium"
            if (highPriorityRadio.isChecked) priority = "high"

            if (checkStatus()) {
                val db = FirebaseFirestore.getInstance()
                val task = hashMapOf(
                    "task" to taskInput.text.toString(),
                    "description" to descriptionInput.text.toString(),
                    "date" to dateText.text.toString(),
                    "priority" to priority,
                    "status" to "pending",
                    "completedDate" to ""
                )

                // Add a new document with a generated ID
                db.collection("tasks")
                    .add(task)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        pickDate()
    }

    private fun getDateTimeCalendar() {
        val calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
    }

    private fun pickDate() {
        datePicker.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        dateText.text = "$savedDay-$savedMonth-$savedYear"

    }

    private fun checkStatus() : Boolean {
        if (taskInput.text.isBlank()) {
            requiredText.visibility = View.VISIBLE
            return false
        }

        return true
    }

}