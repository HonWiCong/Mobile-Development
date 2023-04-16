package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(private val places: ArrayList<Place>, private val context: Context) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    private lateinit var listener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    class PlaceViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val placeIcon: ImageView = itemView.findViewById(R.id.place_icon)
        val placeName: TextView = itemView.findViewById(R.id.name)
        val ratingIcon: ImageView = itemView.findViewById(R.id.rating_icon)
        val rating: TextView = itemView.findViewById(R.id.rate)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(itemView, listener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val currentPlace = places[position]
        holder.placeName.text = currentPlace.name
        holder.rating.text = currentPlace.rating.toString() + " stars"

        val drawableName = currentPlace.images.icon
        val resourceId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)

        holder.placeIcon.setImageResource(resourceId)
        holder.ratingIcon.setImageResource(R.drawable.rating)
    }

    override fun getItemCount(): Int {
        return places.size
    }
}