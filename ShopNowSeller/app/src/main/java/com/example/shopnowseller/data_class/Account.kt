package com.example.shopnowseller.data_class

data class Account(
    val username: String? = "",
    val email: String = "",
    val address: String? = "",
    val image: String? = "",
    var cart_list: ArrayList<String>? = ArrayList()
)

