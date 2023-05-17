package com.example.assignment6

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lat = 13.409808180753316
        val long = 52.51894887928074
        val key = "65afe04d18ae4fa8aeed2ade0ca9f6e3"

        val client = OkHttpClient()

        CoroutineScope(Dispatchers.IO).launch {
            val request = Request.Builder()
                .url("https://api.geoapify.com/v1/geocode/reverse?lat=$lat&lon=$long&format=json&apiKey=$key")
                .method("GET", null)
                .build()
            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()

            CoroutineScope(Dispatchers.Main).launch {
                val textView = findViewById<TextView>(R.id.textView)
                textView.text = responseBody
            }
        }
    }
}