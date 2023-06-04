package com.example.shopnow.data_class

import android.os.Parcel
import android.os.Parcelable

data class Order(
    var product_id : String = "",
    var quantity : Int = 1,
    var buyer_id: String = "",
    var buyer_name: String = "",
    var seller_id: String = "",
    var totalPrice: Double = 0.0,
    var date: String = "",
    var delivery_fee: Double = 10.0,
    var payment: String = "",
    var address: String = "No. 159, Lorong 15, Jalan Arang, 93250\nKuching, Sarawak, 93250, Kuching, Sarawak",
    var product_name: String = "",
    var product_image: String = "",
    var product_price: Double = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(product_id)
        parcel.writeInt(quantity)
        parcel.writeString(buyer_id)
        parcel.writeString(buyer_name)
        parcel.writeString(seller_id)
        parcel.writeDouble(totalPrice)
        parcel.writeString(date)
        parcel.writeDouble(delivery_fee)
        parcel.writeString(payment)
        parcel.writeString(address)
        parcel.writeString(product_name)
        parcel.writeString(product_image)
        parcel.writeDouble(product_price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}