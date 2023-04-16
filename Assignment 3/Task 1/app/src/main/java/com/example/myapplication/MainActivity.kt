package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.title = "Co-Working Space Access Pass"
        init()
    }

    private var placeList = ArrayList<Place>()


    private lateinit var recyclerView: RecyclerView

    private fun init() {
        recyclerView = findViewById(R.id.main_list)

        placeList = readPlaceFromJson()

        var adapter = PlaceAdapter(placeList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: PlaceAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                redirectToBookingPage(position)
            }
        })
    }

    private fun redirectToBookingPage(index : Int) {
        val intent = Intent(this, booking_page::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun readPlaceFromJson(): ArrayList<Place> {
        val jsonString = applicationContext.assets.open("place_details.json").bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, Array<Place>::class.java).toList() as ArrayList<Place>
    }



}