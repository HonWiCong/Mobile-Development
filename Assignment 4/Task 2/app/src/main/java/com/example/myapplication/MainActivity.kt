package com.example.myapplication

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Spinner
import com.bumptech.glide.Glide
import com.example.myapplication.adapter.GridImageAdapter
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

class MainActivity : AppCompatActivity() {
    private var selectedOptions = ""
    val storageRef = Firebase.storage.reference
    val self = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Photo Album"

        val imageView = findViewById<ImageView>(R.id.imageView3)
        val gridView: GridView = findViewById(R.id.grid_view)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val category = resources.getStringArray(R.array.category)
        if (spinner != null) {
            val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, category)
            spinner.adapter = spinnerAdapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        selectedOptions = "all"
                        val imageRefs = listOf(
                            storageRef.child("/creativity/thumbnail"),
                            storageRef.child("/hygge/thumbnail"),
                            storageRef.child("/vintage/thumbnail")
                        )

                        val urlList = ArrayList<Uri>()
                        val tasks = mutableListOf<Task<*>>()
                        for (ref in imageRefs) {
                            tasks.add(ref.listAll().addOnSuccessListener { results ->
                                results.items.forEach { item ->
                                    tasks.add(item.downloadUrl.addOnSuccessListener { url ->
                                        urlList.add(url)
                                    })
                                }
                            }.addOnFailureListener { })
                        }
                        
                        Tasks.whenAllComplete(tasks).addOnCompleteListener {
                            gridView.adapter = GridImageAdapter(self, urlList)
                            if (urlList.isNotEmpty()) {
                                Glide.with(self)
                                    .load(urlList[0])
                                    .into(imageView)
                            }
                        }


                    }

                    1 -> {

                    }

                    2 -> {
                        selectedOptions = "hygge"

                    }

                    3 -> {
                        selectedOptions = "vintage"

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    private fun getAllThumbnailsURL() : ArrayList<String> {
        var storageRef = FirebaseStorage.getInstance().reference
        val thumbnailRef = storageRef.child("thumbnail")
        val imageList = ArrayList<String>()

        thumbnailRef.listAll().addOnSuccessListener { listResult ->
            listResult.prefixes.forEach { folderRef ->
                val imagesRef = folderRef.child("thumbnail")

                imagesRef.listAll().addOnSuccessListener { listResult ->
                    listResult.items.forEach { item ->
                        item.downloadUrl.addOnSuccessListener { uri ->
                            imageList.add(uri.toString())
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting images from Firebase Storage: ${exception.message}")
                }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting subfolders from Firebase Storage: ${exception.message}")
        }

        return imageList
    }
}

