package com.example.shopnow.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.adapter.PurchasedRecyclerViewAdapter
import com.example.shopnow.data_class.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG


class ToShipFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val database = FirebaseFirestore.getInstance()

    // Declare the adapter and orderList as class variables
    private lateinit var adapter: PurchasedRecyclerViewAdapter
    private val orderList = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_to_ship, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.to_ship_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the adapter
        adapter = PurchasedRecyclerViewAdapter({ orderList }, requireContext(), "To Ship")
        recyclerView.adapter = adapter

        database
            .collection("orders")
            .whereEqualTo("buyer_id", currentUser!!.uid)
            .whereEqualTo("status", "To Ship")
            .whereEqualTo("visibility", true)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                orderList.clear()

                for (document in snapshots!!) {
                    val order = document.toObject<Order>()
                    order.id = document.id
                    if (order.buyer_id == currentUser!!.uid) {
                        orderList.add(order)
                    }
                }

                val text = view.findViewById<TextView>(R.id.no_order_text)
                text.visibility = if (orderList.isNotEmpty()) View.GONE else View.VISIBLE

                adapter.notifyDataSetChanged()
            }

        return view
    }

    companion object {
        private const val ARG_TAB_TITLE = "arg_tab_title"

        fun newInstance(tabTitle: String): ToShipFragment {
            val fragment = ToShipFragment()
            val args = Bundle()
            args.putString(ARG_TAB_TITLE, tabTitle)
            fragment.arguments = args
            return fragment
        }
    }
}
