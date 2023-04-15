package com.example.myapplication

data class Place(
    val id: Int,
    val name: String,
    val rating: String,
    val facilities: String,
    val passRates: String,
    val images: Image
)

data class Image(
    val icon: String,
    val cover: String
)
