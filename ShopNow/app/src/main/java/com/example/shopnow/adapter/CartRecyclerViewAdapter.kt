package com.example.shopnow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activity.ProductDetailActivity
import com.example.shopnow.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CartRecyclerViewAdapter(private var productList: ArrayList<Product>, private val context: Context, val handler: (ArrayList<String>) -> Unit) : RecyclerView.Adapter<CartRecyclerViewAdapter.CartItemViewHolder>() {
    private var selectedCartProductList = ArrayList<String>()
    private val database = FirebaseFirestore.getInstance()


    inner class CartItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.cart_image)
        val name = item.findViewById<TextView>(R.id.cart_name)
        val price = item.findViewById<TextView>(R.id.cart_price)
        val container = item.findViewById<ConstraintLayout>(R.id.card_item_layout)
        val checkbox = item.findViewById<CheckBox>(R.id.cart_checkbox)
        val quantity = item.findViewById<TextView>(R.id.cart_item_count)
        val minus = item.findViewById<ImageView>(R.id.cart_minus_count)
        val plus = item.findViewById<ImageView>(R.id.bottom_sheet_add_count)
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
            .into(holder.image)

        holder.price.text = "RM${product.price}"

        holder.container.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }

        holder.checkbox.setOnClickListener {
            if (holder.checkbox.isChecked) {
                selectedCartProductList.add(product.id!!)
            } else {
                selectedCartProductList.remove(product.id!!)
            }
            handler.invoke(selectedCartProductList)
        }

        val duplicateCount = productList.count { it.id == product.id }
        holder.quantity.text = duplicateCount.toString()

        if (duplicateCount > 1 && position != productList.indexOfFirst { it.id == product.id }) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }


        val currentUser = FirebaseAuth.getInstance().currentUser
        holder.minus.setOnClickListener {
            if (duplicateCount > 1) {
                // Extract the product IDs from the object list
                val productIds = productList.mapNotNull { it.id }

                // Remove the first occurrence of the duplicated product ID
                val updatedProductIds = productIds.toMutableList()
                updatedProductIds.remove(product.id)

                // Update the database with the modified product ID array
                database
                    .collection("accounts")
                    .document(currentUser!!.uid)
                    .update("cart_list", updatedProductIds)
                    .addOnSuccessListener {
                        // Find the index of the duplicated product
                        val firstIndex = productList.indexOfFirst { it.id == product.id }

                        // Remove the duplicated product from the object list
                        if (firstIndex != -1) {
                            productList.removeAt(firstIndex)
                            notifyDataSetChanged()
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle the failure case
                        Toast.makeText(context, "Failed to update the product IDs: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }


        holder.plus.setOnClickListener {
            val maxQuantity = product.quantity

            // Extract the product IDs from the object list
            val productIds = productList.mapNotNull { it.id }

            // Check if the cart item quantity is already at the maximum allowed
            if (productIds.count { it == product.id } < maxQuantity!!) {
                // Add one more duplicated ID to the list
                val updatedProductIds = productIds.toMutableList()
                updatedProductIds.add(product.id!!)

                // Update the database with the modified product ID array
                database
                    .collection("accounts")
                    .document(currentUser!!.uid)
                    .update("cart_list", updatedProductIds)
                    .addOnSuccessListener {
                        // Add one more duplicated product to the object list
                        productList.add(product.copy())
                        notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        // Handle the failure case
                        Toast.makeText(context, "Failed to update the product IDs: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Display a message indicating that the maximum quantity has been reached
                Toast.makeText(context, "Maximum quantity reached", Toast.LENGTH_SHORT).show()
            }
        }


    }




    override fun getItemCount(): Int {
        return productList.size
    }
}
