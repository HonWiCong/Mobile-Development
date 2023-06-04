package com.example.shopnow.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopnow.R
import com.example.shopnow.adapter.CartRecyclerViewAdapter
import com.example.shopnow.data_class.Account
import com.example.shopnow.data_class.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    private val accountsCollection = db.collection("accounts")

    private lateinit var cartRecyclerView : RecyclerView
    private var selectedItem = ArrayList<String>()
    private val cartProducts = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cartRecyclerView = view.findViewById(R.id.cart_recycler_view)
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val deleteButton = view.findViewById<ImageView>(R.id.cart_delete)
        deleteButton.setOnClickListener {
            deleteEvent()
        }

        getAccountCartItems()
        return view
    }

    private fun getAccountCartItems() {
        currentUser?.let { account ->
            val accountDocumentRef = accountsCollection.document(account.uid)

            accountDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val account = documentSnapshot.toObject(Account::class.java)

                        account?.cart_list?.let { cartList ->
                            val productsCollectionRef = db.collection("products")

                            for (productId in cartList) {
                                productsCollectionRef.document(productId).get()
                                    .addOnSuccessListener { productDocument ->
                                        if (productDocument.exists()) {

                                            val product = productDocument.toObject(Product::class.java)
                                            product?.id = productDocument.id
                                            product?.let { cartProducts.add(it) }
                                            cartRecyclerView.adapter = CartRecyclerViewAdapter(cartProducts, requireContext()) {
                                                selectedItem = it
                                            }
                                        } else {
                                            println("Product document does not exist for ID: $productId")
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        println("Error retrieving product document for ID: $productId, Error: $e")
                                    }
                            }

                        } ?: run {
                            println("Cart list is empty")
                        }
                    } else {
                        println("User document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    println("Error retrieving user document: $e")
                }
        }
    }

    private fun deleteEvent() {
        currentUser?.let { account ->
            val accountDocumentRef = accountsCollection.document(account.uid)

            accountDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val account = documentSnapshot.toObject(Account::class.java)

                        var cartList = account?.cart_list
                        cartList?.removeAll(selectedItem)

                        val updatedData = hashMapOf(
                            "cart_list" to cartList
                        )

                        if (selectedItem.isNotEmpty()) {
                            accountDocumentRef
                                .update(updatedData as Map<String, Any>)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Removed selected item", Toast.LENGTH_SHORT).show()

                                    val modifiedCartProductList = removeProductsById(cartProducts, selectedItem)
                                    cartRecyclerView.adapter = CartRecyclerViewAdapter(modifiedCartProductList, requireContext()) {}
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error removing selected item: $e", Toast.LENGTH_SHORT).show()
                                }

                        } else {
                            Toast.makeText(context, "Please select item(s)", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        println("User document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    println("Error retrieving user document: $e")
                }
        }
    }

    private fun removeProductsById(products: ArrayList<Product>, ids: ArrayList<String>): ArrayList<Product> {
        val iterator = products.iterator()
        while (iterator.hasNext()) {
            val product = iterator.next()
            if (ids.contains(product.id)) {
                iterator.remove()
            }
        }
        return products
    }

}