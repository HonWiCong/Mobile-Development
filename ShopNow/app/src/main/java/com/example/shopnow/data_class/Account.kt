package com.example.shopnow.data_class

data class Account(
    val username: String,
    val email: String,
    val address: String?,
    val cart_list: ArrayList<Product>
)
