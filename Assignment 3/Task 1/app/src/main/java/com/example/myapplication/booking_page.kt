package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.gson.Gson


class booking_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_page)
        init()
    }

    private lateinit var name: TextView
    private lateinit var cover: ImageView
    private lateinit var amenitiesText: TextView
    private lateinit var spinner: Spinner
    private lateinit var quantityValue: EditText
    private lateinit var totalValue: TextView
    private lateinit var orderButton: Button

    private var passRatePrice = 0
    private var placeList = ArrayList<Place>()
    private var passRates = PassRate()
    private var totalPrice = 0
    private var type = "Daily Pass"
    private var resourceId = 0
    private var index = 0

    private fun init() {
        // Element
        cover = findViewById(R.id.booking_page_cover)
        name = findViewById(R.id.booking_page_name)
        amenitiesText = findViewById(R.id.amenities_text)
        spinner = findViewById(R.id.spinner)
        quantityValue = findViewById(R.id.quantity_value)
        totalValue = findViewById(R.id.total_value)
        orderButton = findViewById(R.id.order_button)


        // Data from Json
        index = intent.getIntExtra("index", 0)
        placeList = readPlaceFromJson()
        val place = placeList[index]
        passRates = place.pass_rates


        // set content to element
        resourceId = resources.getIdentifier(place.images.cover, "drawable", packageName)
        cover.setImageResource(resourceId)
        name.text = place.name
        amenitiesText.text = place.facilities

        // spinner
        val passRatesItems = listOf(
            "Daily Pass - RM ${passRates.daily}",
            "Weekly Pass - RM ${passRates.weekly}",
            "Monthly Pass - RM ${passRates.monthly}"
        )

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, passRatesItems)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                if (selectedItem.contains("Daily")) type = "Daily Pass"
                else if (selectedItem.contains("Weekly")) type = "Weekly Pass"
                else if (selectedItem.contains("Monthly")) type = "Monthly Pass"

                val regex = "\\d+".toRegex()
                val matchResult = regex.find(selectedItem)

                if (matchResult != null) {
                    passRatePrice = matchResult.groupValues[0].toInt()
                    totalPrice = passRatePrice * quantityValue.text.toString().toInt()
                    totalValue.text = "RM $totalPrice"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        quantityValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    totalPrice = passRatePrice * quantityValue.text.toString().toInt()
                    totalValue.text = "RM $totalPrice"

                } catch (e: NumberFormatException) {
                    // handle the exception
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }
        })

        orderButton.setOnClickListener{
            val intent = Intent(this, order_complete::class.java)
            intent.putExtra("totalPrice", totalPrice)
            intent.putExtra("type", type)
            intent.putExtra("quantity", quantityValue.text.toString().toInt())
            intent.putExtra("resourceId", resourceId)
            intent.putExtra("name", name.text)

            startActivity(intent)
        }
    }

    private fun readPlaceFromJson(): ArrayList<Place> {
        val jsonString = applicationContext.assets.open("place_details.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, Array<Place>::class.java).toList() as ArrayList<Place>
    }
}

