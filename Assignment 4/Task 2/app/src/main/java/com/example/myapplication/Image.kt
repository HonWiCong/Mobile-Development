package com.example.myapplication

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Image(
    var thumbnail: Uri?,
    var image: Uri?,
    var photographer: String?,
    var category: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(thumbnail, flags)
        parcel.writeParcelable(image, flags)
        parcel.writeString(photographer)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}
