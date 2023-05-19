package com.example.shopnow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activity.ProductDetailActivity
import com.example.shopnow.data_class.Product



class CartRecyclerViewAdapter(private val productList: ArrayList<Product>, private val context: Context, val handler: (ArrayList<String>) -> Unit) : RecyclerView.Adapter<CartRecyclerViewAdapter.CartItemViewHolder>() {
    private var selectedCartProductList = ArrayList<String>()

    inner class CartItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.cart_image)
        val name = item.findViewById<TextView>(R.id.cart_name)
        val price = item.findViewById<TextView>(R.id.cart_price)
        val container = item.findViewById<ConstraintLayout>(R.id.card_item_layout)
        val checkbox = item.findViewById<CheckBox>(R.id.cart_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val product = productList[position]

        holder.name.text = product.name

        Glide.with(context)
            .load(product.image)
            .into(holder.image);

        holder.price.text = "RM${product.price}"

        holder.container.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }

        holder.checkbox.setOnClickListener {
            if (holder.checkbox.isChecked) {
                selectedCartProductList.add(product.id!!)
                handler.invoke(selectedCartProductList)
            } else {
                selectedCartProductList.remove(product.id!!)
                handler.invoke(selectedCartProductList)
            }
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
