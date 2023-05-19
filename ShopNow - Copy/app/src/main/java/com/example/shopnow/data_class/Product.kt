package com.example.shopnow.data_class

import android.os.Parcel
import android.os.Parcelable

data class Product(
    var id: String? = "",
    var name: String = "",
    var price: Double = 0.0,
    var image: String? = "",
    var rating: String? = "",
    var description: String? = "",
    var seller: String? = "",
    var seller_thumbnail: String? = "",
    var category: String? = "",
    var status: Boolean? = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(image)
        parcel.writeString(rating)
        parcel.writeString(description)
        parcel.writeString(seller)
        parcel.writeString(seller_thumbnail)
        parcel.writeString(category)
        parcel.writeValue(status)
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
