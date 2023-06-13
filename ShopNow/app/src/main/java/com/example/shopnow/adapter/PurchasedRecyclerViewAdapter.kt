package com.example.shopnow.adapter

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopnow.R
import com.example.shopnow.activity.CancellationDetailActivity
import com.example.shopnow.activity.OrderDetailActivity
import com.example.shopnow.activity.ProductDetailActivity
import com.example.shopnow.data_class.Order
import com.example.shopnow.data_class.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PurchasedRecyclerViewAdapter(private val getOrderList: () -> List<Order>, private val context: Context, private val type: String) : RecyclerView.Adapter<PurchasedRecyclerViewAdapter.ProductItemViewHolder>() {
    private val database = Firebase.firestore

    inner class ProductItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image = item.findViewById<ImageView>(R.id.purchased_product_image)
        val name = item.findViewById<TextView>(R.id.purchased_product_name)
        val price = item.findViewById<TextView>(R.id.purchased_product_price)
        val quantity = item.findViewById<TextView>(R.id.purchased_quantity)
        val total = item.findViewById<TextView>(R.id.purchased_total)
        val button = item.findViewById<Button>(R.id.purchase_item_button)
        val container = item.findViewById<ConstraintLayout>(R.id.purchase_product_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.purchased_item, parent, false)
        return ProductItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        val order = getOrderList()[position]

        holder.name.text = order.product_name

        Glide.with(context)
            .load(order.product_image)
            .into(holder.image);

        holder.price.text = "RM" + order.product_price.toString()
        holder.quantity.text = "Qty: " + order.quantity.toString()
        holder.total.text = "RM" + order.totalPrice.toString()

        holder.container.setOnClickListener {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("order", order)
            context.startActivity(intent)
        }

        if (type == "To Ship") {
            holder.button.text = "Cancel Order"

            holder.button.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle("Cancel Order")
                    setMessage("Are you sure you want to cancel this order?")
                    setPositiveButton("Yes") { _, _ ->
                        database
                            .collection("orders")
                            .document(order.id)
                            .update("status", "Cancelled")
                            .addOnSuccessListener {
                                Toast.makeText(context, "You have cancelled the order.", Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged() // update the adapter
                            }
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
            }
        }

        if (type == "To Receive") {
            holder.button.text = "Order Received"
            holder.button.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle("Order received")
                    setMessage("Are you sure you have received this order? We will release the money to the seller if you have confirmed this.")
                    setPositiveButton("Yes") { _, _ ->
                        database
                            .collection("orders")
                            .document(order.id)
                            .update("status", "Completed")
                            .addOnSuccessListener {
                                Toast.makeText(context, "You have updated the status.", Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged() // update the adapter
                            }
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
            }
        }

        if (type == "Completed") {
            val orderId = order.id

            database
                .collection("orders")
                .document(orderId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val order = documentSnapshot.toObject<Order>()
                    if (order?.rate != null) {
                        holder.button.visibility = View.GONE
                    } else {
                        holder.button.text = "Rate"

                        holder.button.setOnClickListener {
                            val dialogBuilder = AlertDialog.Builder(context)
                            val inflater = LayoutInflater.from(context)
                            val dialogView = inflater.inflate(R.layout.rating_dialog, null)

                            dialogBuilder.setView(dialogView)
                            dialogBuilder.setCancelable(true)

                            val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
                            val submitButton = dialogView.findViewById<Button>(R.id.submitBtn)

                            val alertDialog = dialogBuilder.create()

                            submitButton.setOnClickListener {
                                val selectedId = radioGroup.checkedRadioButtonId
                                val radioButton = dialogView.findViewById<RadioButton>(selectedId)
                                val ratingValue = radioButton.text.toString().toDouble()

                                database
                                    .collection("orders")
                                    .document(orderId)
                                    .update("rate", ratingValue)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Thanks for your rating", Toast.LENGTH_SHORT).show()
                                        notifyDataSetChanged()
                                    }

                                alertDialog.dismiss()
                            }

                            alertDialog.show()
                        }
                    }
                }
        }





        if (type == "Cancelled") {
            holder.button.text = "Delete Order"
            holder.button.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle("Delete Order")
                    setMessage("Are you sure you want to remove this order?")
                    setPositiveButton("Yes") { _, _ ->
                        database
                            .collection("orders")
                            .document(order.id)
                            .update("visibility", false)
                            .addOnSuccessListener {
                                Toast.makeText(context, "You have deleted the order.", Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged() // update the adapter
                            }
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
            }

            holder.container.setOnClickListener {
                val intent = Intent(context, CancellationDetailActivity::class.java)
                intent.putExtra("order", order)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return getOrderList().size
    }

}