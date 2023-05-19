package com.example.shopnowseller.adapter

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
import com.example.shopnowseller.data_class.Product

class ProductRecyclerViewAdapter(private val productList: ArrayList<Product>, private val context: Context) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.item_product_image)
        val name = item.findViewById<TextView>(R.id.item_product_name)
        val price = item.findViewById<TextView>(R.id.item_product_price)
        val category = item.findViewById<TextView>(R.id.item_product_category)
        val status = item.findViewById<TextView>(R.id.item_product_status)
        val container = item.findViewById<ConstraintLayout>(R.id.item_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val product = productList[position]

        Glide.with(context)
            .load(product.image)
            .into(holder.image)

        holder.name.text = product.name
        holder.price.text = "RM" + product.price.toString()
        holder.category.text = product.category

        if (product.status!!) {
            holder.status.text = "Active"
        } else {
            holder.status.text = "Inactive"
        }

        holder.container.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }
    }
}