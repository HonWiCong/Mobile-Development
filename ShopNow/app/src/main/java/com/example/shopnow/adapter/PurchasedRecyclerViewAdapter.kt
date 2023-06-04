package com.example.shopnow.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activity.ProductDetailActivity
import com.example.shopnow.data_class.Order
import com.example.shopnow.data_class.Product

class PurchasedRecyclerViewAdapter(private val orderList: MutableList<Order>, private val context: Context) : RecyclerView.Adapter<PurchasedRecyclerViewAdapter.ProductItemViewHolder>() {
    inner class ProductItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.purchased_product_image)
        val name = item.findViewById<TextView>(R.id.purchased_product_name)
        val price = item.findViewById<TextView>(R.id.purchased_product_price)
        val quantity = item.findViewById<TextView>(R.id.purchased_quantity)
        val total = item.findViewById<TextView>(R.id.purchased_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.purchased_item, parent, false)
        return ProductItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        val order = orderList[position]

        holder.name.text = order.product_name

        Glide.with(context)
            .load(order.product_image)
            .into(holder.image);

        holder.price.text = "RM" + order.product_price.toString()
        holder.quantity.text = "Qty: " + order.quantity.toString()
        holder.total.text = "RM" + order.totalPrice.toString()

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}