package com.example.shopnow.data_class

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val name: String,
    val price: Double,
    val image: String?,
    val rating: String?,
    val description: String?,
    val seller: String?,
    val seller_thumbnail: String?,
    val category: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(image)
        parcel.writeString(rating)
        parcel.writeString(description)
        parcel.writeString(seller)
        parcel.writeString(seller_thumbnail)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
