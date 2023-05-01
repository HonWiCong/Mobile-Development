package com.example.assignment4_task1.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.assignment4_task1.News
import com.example.assignment4_task1.R
import com.example.assignment4_task1.adapters.RecycleViewAdapter


class TechnologyFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsItemList: ArrayList<News>
    private lateinit var newsItemAdapter: RecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_technology, container, false)
        newsItemList = ArrayList()
        recyclerView = view.findViewById(R.id.technology_recycle_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val text = readTextFromAssets(view.context, "task1_data.txt")
        newsItemList = addToList(text)

        newsItemAdapter = RecycleViewAdapter(newsItemList, view.context)
        recyclerView.adapter = newsItemAdapter
        return view
    }

    fun readTextFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
    }

    fun addToList(text: String) : ArrayList<News> {
        val newsList = ArrayList<News>()
        val lines = text.lines()
        var currentNews = News("", "", "", "")
        var counter = 0

        for (line in lines) {
            val parts = line.split(":")

            when (parts[0]) {
                "Title" -> {
                    currentNews.title = line.substringAfter(":")
                }
                "Url" -> {
                    currentNews.url = line.substringAfter(":")
                }
                "Image" -> {
                    currentNews.imageUrl = line.substringAfter(":")
                }
                "Category" -> {
                    currentNews.category = line.substringAfter(":")
                }
            }
            counter++
            if (counter > 3) {
                if (currentNews.category == "Technology") {
                    newsList.add(currentNews)
                }
                counter = 0
                currentNews = News("", "", "", "")
            }

        }

        return newsList
    }


}