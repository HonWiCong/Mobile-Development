package com.example.shopnowseller.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnowseller.R
import com.example.shopnowseller.activity.EditActivity
import com.example.shopnowseller.activity.OrderDetailActivity
import com.example.shopnowseller.data_class.Order
import com.example.shopnowseller.data_class.Product
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class OrderRecyclerViewAdapter(private val orderList: ArrayList<Order>, private val context: Context, private val type: String) : RecyclerView.Adapter<OrderRecyclerViewAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.order_product_image)
        val name = item.findViewById<TextView>(R.id.order_product_name)
        val total = item.findViewById<TextView>(R.id.order_total)
        val date = item.findViewById<TextView>(R.id.order_date)
        val quantity = item.findViewById<TextView>(R.id.order_quantity)
        val container = item.findViewById<ConstraintLayout>(R.id.order_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val order = orderList[position]

        Glide.with(context)
            .load(order.product_image)
            .into(holder.image)

        holder.name.text = order.product_name
        holder.total.text = "RM" + order.totalPrice

        val firebaseTimestamp: Timestamp = order.date!!
        val date: Date = firebaseTimestamp.toDate()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault())
        val formattedDate: String = dateFormat.format(date)
        holder.date.text = formattedDate

        holder.quantity.text = "Qty: " + order.quantity

        holder.container.setOnClickListener {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

}