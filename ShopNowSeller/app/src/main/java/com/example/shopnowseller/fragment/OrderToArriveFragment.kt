package com.example.shopnowseller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnowseller.R
import com.example.shopnowseller.adapter.OrderRecyclerViewAdapter
import com.example.shopnowseller.data_class.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

class OrderToArriveFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_to_arrive, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.to_arrive_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val database = FirebaseFirestore.getInstance()
        val orderList = ArrayList<Order>()
        val id = FirebaseAuth.getInstance().currentUser!!.uid

        database
            .collection("orders")
            .whereEqualTo("seller_id", id)
            .whereEqualTo("status", "To Receive")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val order = document.toObject<Order>()
                    order.id = document.id
                    orderList.add(order)
                }
                recyclerView.adapter = OrderRecyclerViewAdapter(orderList, view.context, "To Receive")
            }

        return view
    }

    companion object {
        private const val ARG_TAB_TITLE = "arg_tab_title"

        fun newInstance(tabTitle: String): OrderToArriveFragment {
            val fragment = OrderToArriveFragment()
            val args = Bundle()
            args.putString(ARG_TAB_TITLE, tabTitle)
            fragment.arguments = args
            return fragment
        }
    }
}