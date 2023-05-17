package com.example.assignment7.data_class

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Recipe (

//    @SerializedName("uri"             ) var uri             : String?                = null,
    @SerializedName("label"           ) var label           : String?                = null,
    @SerializedName("image"           ) var image           : String?                = null,
    @SerializedName("images"          ) var images          : Images?                = Images(),
    @SerializedName("source"          ) var source          : String?                = null,
//    @SerializedName("url"             ) var url             : String?                = null,
//    @SerializedName("shareAs"         ) var shareAs         : String?                = null,
//    @SerializedName("yield"           ) var yield           : Int?                   = null,
    @SerializedName("dietLabels"      ) var dietLabels      : ArrayList<String>      = arrayListOf(),
//    @SerializedName("healthLabels"    ) var healthLabels    : ArrayList<String>      = arrayListOf(),
//    @SerializedName("cautions"        ) var cautions        : ArrayList<String>      = arrayListOf(),
//    @SerializedName("ingredientLines" ) var ingredientLines : ArrayList<String>      = arrayListOf(),
    @SerializedName("ingredients"     ) var ingredients     : ArrayList<Ingredients> = arrayListOf(),
//    @SerializedName("calories"        ) var calories        : Double?                = null,
//    @SerializedName("totalWeight"     ) var totalWeight     : Double?                = null,
//    @SerializedName("totalTime"       ) var totalTime       : Int?                   = null,
//    @SerializedName("cuisineType"     ) var cuisineType     : ArrayList<String>      = arrayListOf(),
//    @SerializedName("mealType"        ) var mealType        : ArrayList<String>      = arrayListOf(),
//    @SerializedName("dishType"        ) var dishType        : ArrayList<String>      = arrayListOf(),

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

//data class Ingredients (
//
//    @SerializedName("text"         ) var text         : String? = null,
////    @SerializedName("quantity"     ) var quantity     : Int?    = null,
////    @SerializedName("measure"      ) var measure      : String? = null,
////    @SerializedName("food"         ) var food         : String? = null,
////    @SerializedName("weight"       ) var weight       : Int?    = null,
////    @SerializedName("foodCategory" ) var foodCategory : String? = null,
////    @SerializedName("foodId"       ) var foodId       : String? = null,
////    @SerializedName("image"        ) var image        : String? = null
//
//)
