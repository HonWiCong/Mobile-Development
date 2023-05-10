package com.example.myapplication

import android.content.ContentValues.TAG
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Spinner
import com.example.myapplication.adapter.GridImageAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private val storageRef = Firebase.storage.reference
    val self = this
    private val ImageList = ArrayList<Image>()
    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Photo Album"

        gridView= findViewById(R.id.grid_view)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val category = resources.getStringArray(R.array.category)
        if (spinner != null) {
            val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, category)
            spinner.adapter = spinnerAdapter
        }
        getAllImage()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        gridView.adapter = GridImageAdapter(self, ImageList)
                    }

                    1 -> {
                        val tempList = ArrayList<Image>()
                        for (image in ImageList) {
                            if (image.category == "creativity") {
                                tempList.add(image)
                            }
                        }

                        gridView.adapter = GridImageAdapter(self, tempList)
                    }

                    2 -> {
                        val tempList = ArrayList<Image>()
                        for (image in ImageList) {
                            if (image.category == "hygge") {
                                tempList.add(image)
                            }
                        }

                        gridView.adapter = GridImageAdapter(self, tempList)
                    }

                    3 -> {
                        val tempList = ArrayList<Image>()
                        for (image in ImageList) {
                            if (image.category == "vintage") {
                                tempList.add(image)
                            }
                        }

                        gridView.adapter = GridImageAdapter(self, tempList)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun getAllImage() {
        val folderPath = listOf(
            "creativity",
            "hygge",
            "vintage",
        )
        val thumbnailUrls = HashMap<String, Uri>()
        val imageUrls = HashMap<String, Uri>()

        for (path in folderPath) {
            storageRef.child("/$path/thumbnail").listAll().addOnSuccessListener { results ->
                results.items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener{ thumbnail_url ->
                        val photographerName = item.name
                            .substringBeforeLast("_thumb.jpg")
                            .replace("_", " ")
                            .split(" ")
                            .joinToString(" ") { it.capitalize() }

                        ImageList.add(Image(thumbnail_url, null, photographerName, path))
                    }.addOnFailureListener { e -> Log.e("Error", "Failed to get thumbnail URL: ${e.message}") }
                }
            }.addOnFailureListener { e -> Log.e("Error", "Failed to list items: ${e.message}") }

            storageRef.child("$path/cover").listAll().addOnSuccessListener { results ->
                results.items.forEach { item ->
                    item.downloadUrl.addOnSuccessListener { image_url ->
                        val photographerName = item.name
                            .substringBeforeLast(".jpg")
                            .replace("_", " ")
                            .split(" ")
                            .joinToString(" ") { it.capitalize() }

                        for (image in ImageList) {
                            if (image.image == null && image.photographer == photographerName) {
                                image.image = image_url
                                gridView.adapter = GridImageAdapter(this, ImageList)
                            }
                        }
                    }.addOnFailureListener { e -> Log.e("Error", "Failed to get cover URL: ${e.message}") }
                }
            }.addOnFailureListener { e -> Log.e("Error", "Failed to list items: ${e.message}") }

        }

    }
}

