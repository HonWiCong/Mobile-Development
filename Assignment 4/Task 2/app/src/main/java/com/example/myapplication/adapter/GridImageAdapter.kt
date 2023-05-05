package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.example.myapplication.DetailActivity
import com.example.myapplication.Image
import com.example.myapplication.R

class GridImageAdapter(private val context: Context, private val imageList: ArrayList<Image>) : BaseAdapter() {

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Any {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        try {
            Glide.with(context)
                .load(imageList[position].thumbnail)
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.border)
                .override(350, 350)
                .into(viewHolder.image);

            viewHolder.image.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("Image", imageList[position])
                }
                context.startActivity(intent)
            }
        } catch (error: Error) {
            throw error
        }


        return view
    }

    private class ViewHolder(view: View) {
        val image: ImageView = view.findViewById(R.id.grid_image)
    }
}