package com.example.myapplication

data class Place(
    val id: Int,
    val name: String,
    val rating: String,
    val facilities: String,
    val pass_rates: PassRate,
    val images: Image
)

data class PassRate(
    val daily: Int = 0,
    val weekly: Int = 0,
    val monthly: Int = 0
)

data class Image(
    val icon: String,
    val cover: String
)
