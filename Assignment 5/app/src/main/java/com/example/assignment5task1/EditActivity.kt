package com.example.assignment5task1

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var taskInput : EditText
    private lateinit var requiredText : TextView
    private lateinit var descriptionInput : EditText
    private lateinit var dateText : TextView
    private lateinit var datePicker : MaterialCardView
    private lateinit var lowPriorityRadio: RadioButton
    private lateinit var mediumPriorityRadio: RadioButton
    private lateinit var highPriorityRadio: RadioButton
    private lateinit var submitButton: Button
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    private val calendar = Calendar.getInstance()


    var id = ""

    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        requiredText = findViewById(R.id.required_text)
        datePicker = findViewById(R.id.date_picker)

        taskInput = findViewById(R.id.edit_task_input)
        descriptionInput = findViewById(R.id.edit_description_input)
        dateText = findViewById(R.id.edit_date_text)
        lowPriorityRadio = findViewById(R.id.edit_low_priority_radio)
        mediumPriorityRadio = findViewById(R.id.edit_medium_priority_radio)
        highPriorityRadio = findViewById(R.id.edit_high_priority_radio)
        submitButton = findViewById(R.id.edit_submit_button)

        id = intent.getStringExtra("id").toString()
        taskInput.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("task"))
        descriptionInput.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("description"))
        dateText.text = intent.getStringExtra("date")

        when (intent.getStringExtra("priority")) {
            "low" -> { lowPriorityRadio.isChecked = true }
            "medium" -> { mediumPriorityRadio.isChecked = true }
            "high" -> { highPriorityRadio.isChecked = true }
        }

        submitButtonClickListener()
        calendar.time = intent.getStringExtra("date")?.let { dateFormat.parse(it) }!!
        pickDate()
    }

    private fun getDateTimeCalendar() {


        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH) + 1  // Month is zero-based, so add 1
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
        calendar.time = dateFormat.parse("$savedDay-$savedMonth-$savedYear")
    }

    private fun checkStatus() : Boolean {
        if (taskInput.text.isBlank()) {
            requiredText.visibility = View.VISIBLE
            return false
        }

        return true
    }

    private fun submitButtonClickListener() {
        submitButton.setOnClickListener {
            var priority = ""
            if (lowPriorityRadio.isChecked) priority = "low"
            if (mediumPriorityRadio.isChecked) priority = "medium"
            if (highPriorityRadio.isChecked) priority = "high"

            if (checkStatus()) {
                val db = FirebaseFirestore.getInstance()

                // Add a new document with a generated ID
                db.collection("tasks")
                    .document(id)
                    .update(
                        "task", taskInput.text.toString(),
                        "description", descriptionInput.text.toString(),
                        "date", dateText.text.toString(),
                        "priority", priority
                    )

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}