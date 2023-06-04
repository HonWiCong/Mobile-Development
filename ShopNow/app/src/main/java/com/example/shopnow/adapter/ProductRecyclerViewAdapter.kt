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
import com.example.shopnow.data_class.Product

class ProductRecyclerViewAdapter(private val productList: MutableList<Product>, private val context: Context) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductItemViewHolder>() {
    inner class ProductItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.product_image)
        val name = item.findViewById<TextView>(R.id.product_name)
        val price = item.findViewById<TextView>(R.id.product_price)
        val container = item.findViewById<CardView>(R.id.item_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        val product = productList[position]
        Log.d("Product name from adapter: ", product.name!!)

        holder.name.text = product.name

        Glide.with(context)
            .load(product.image)
            .into(holder.image);

        holder.price.text = product.price.toString()

        holder.container.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

}