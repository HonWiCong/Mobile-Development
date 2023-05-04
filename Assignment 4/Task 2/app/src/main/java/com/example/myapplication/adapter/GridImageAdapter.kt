package com.example.myapplication.adapter

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageReference

class GridImageAdapter(private val context: Context, private val images: ArrayList<Uri>) : BaseAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Any {
        return images[position]
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

        Glide.with(context)
            .load(images[position])
            .into(viewHolder.image)

        return view
    }

    private class ViewHolder(view: View) {
        val image: ImageView = view.findViewById(R.id.grid_item_image)
    }
}