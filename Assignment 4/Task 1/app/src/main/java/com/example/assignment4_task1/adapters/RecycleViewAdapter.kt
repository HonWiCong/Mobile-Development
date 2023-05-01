package com.example.assignment4_task1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4_task1.News
import com.example.assignment4_task1.R
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment4_task1.fragments.WebViewFragment

class RecycleViewAdapter(private val newsItemList: ArrayList<News>, private val context: Context) : RecyclerView.Adapter<RecycleViewAdapter.NewsItemViewHolder>() {
    class NewsItemViewHolder(newsItemView: View) : RecyclerView.ViewHolder(newsItemView) {
        val image : ImageView = newsItemView.findViewById(R.id.news_image)
        val title : TextView = newsItemView.findViewById(R.id.news_text)
        val item : LinearLayout = newsItemView.findViewById(R.id.item_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsItemList.size
    }



    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val news = newsItemList[position]
        var resourceId = 0
        when (news.imageUrl) {
            "image1.png"-> { resourceId = R.drawable.image1 }
            "image2.png"-> { resourceId = R.drawable.image2 }
            "image3.png"-> { resourceId = R.drawable.image3 }
            "image4.png"-> { resourceId = R.drawable.image4 }
            "image5.png"-> { resourceId = R.drawable.image5 }
            "image6.png"-> { resourceId = R.drawable.image6 }
            "image7.png"-> { resourceId = R.drawable.image7 }
            "image8.png"-> { resourceId = R.drawable.image8 }
            "image9.png"-> { resourceId = R.drawable.image9 }
        }
        holder.image.setImageResource(resourceId)
        holder.title.text = news.title

        holder.item.setOnClickListener {
            val webViewFragment = WebViewFragment()
            val bundle = Bundle().apply {
                putString("url", news.url)
            }
            webViewFragment.arguments = bundle

            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, webViewFragment)
                .addToBackStack(null)
                .commit()

        }
    }
}