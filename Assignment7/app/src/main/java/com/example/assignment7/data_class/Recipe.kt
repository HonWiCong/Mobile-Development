package com.example.assignment7.data_class

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Recipe (
    @SerializedName("label"           ) var label           : String?                = null,
    @SerializedName("image"           ) var image           : String?                = null,
    @SerializedName("images"          ) var images          : Images?                = Images(),
    @SerializedName("source"          ) var source          : String?                = null,
    @SerializedName("dietLabels"      ) var dietLabels      : ArrayList<String>      = arrayListOf(),
    @SerializedName("ingredients"     ) var ingredients     : ArrayList<Ingredients> = arrayListOf(),
)

data class Ingredients(
    @SerializedName("text") var text: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ingredients> {
        override fun createFromParcel(parcel: Parcel): Ingredients {
            return Ingredients(parcel)
        }

        override fun newArray(size: Int): Array<Ingredients?> {
            return arrayOfNulls(size)
        }
    }
}
