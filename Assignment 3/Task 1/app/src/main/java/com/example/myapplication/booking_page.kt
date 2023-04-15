package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONArray

class booking_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_page)

        var id = intent.getStringExtra("id")?.toInt()
        val name = readPlaceFromJson(1)

        var title = findViewById<TextView>(R.id.booking_page)
        title.text = name
    }

    fun readPlaceFromJson(id: Int?): String? {
        val jsonString = applicationContext.assets.open("place_details.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val places = gson.fromJson(jsonString, Array<Place>::class.java).toList()

        return places[0].name
    }
}

