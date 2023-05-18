package com.example.assignment6

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "longitude") val longitude: String,
    @ColumnInfo(name = "latitude") val latitude: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "image") val image: String,

)
