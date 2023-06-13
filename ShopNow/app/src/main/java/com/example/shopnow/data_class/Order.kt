package com.example.shopnow.data_class

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class Order(
    var id: String = "",
    var product_id : String = "",
    var shipping_id: Int = 0,
    var quantity : Int = 1,
    var buyer_id: String = "",
    var buyer_name: String = "",
    var seller_id: String = "",
    var totalPrice: Double = 0.0,
    var date: Timestamp? = null,
    var delivery_fee: Double = 10.0,
    var payment: String = "",
    var address: String = "No. 159, Lorong 15, Jalan Arang, 93250\nKuching, Sarawak, 93250, Kuching, Sarawak",
    var product_name: String = "",
    var product_image: String = "",
    var product_price: Double = 0.0,
    var status: String = "To Ship",
    var visibility: Boolean = true,
    var rate: Double? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(product_id)
        parcel.writeInt(shipping_id)
        parcel.writeInt(quantity)
        parcel.writeString(buyer_id)
        parcel.writeString(buyer_name)
        parcel.writeString(seller_id)
        parcel.writeDouble(totalPrice)
        parcel.writeParcelable(date, flags)
        parcel.writeDouble(delivery_fee)
        parcel.writeString(payment)
        parcel.writeString(address)
        parcel.writeString(product_name)
        parcel.writeString(product_image)
        parcel.writeDouble(product_price)
        parcel.writeString(status)
        parcel.writeByte(if (visibility) 1 else 0)
        parcel.writeValue(rate)
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