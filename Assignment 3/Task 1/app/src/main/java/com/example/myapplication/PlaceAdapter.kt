package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(private val places: ArrayList<Place>, private val context: Context) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeIcon: ImageView = itemView.findViewById(R.id.place_icon)
        val placeName: TextView = itemView.findViewById(R.id.name)
        val ratingIcon: ImageView = itemView.findViewById(R.id.rating_icon)
        val rating: TextView = itemView.findViewById(R.id.rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val currentPlace = places[position]
        holder.placeName.text = currentPlace.name
        holder.rating.text = currentPlace.rating.toString()

        val drawableName = currentPlace.images.icon
        val resourceId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)

        holder.placeIcon.setImageResource(resourceId)
        holder.ratingIcon.setImageResource(R.drawable.rating)
    }

    override fun getItemCount(): Int {
        return places.size
    }
}