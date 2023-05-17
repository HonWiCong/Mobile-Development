package com.example.assignment7.data_class

import com.google.gson.annotations.SerializedName

data class Images (

    @SerializedName("THUMBNAIL" ) var THUMBNAIL : IMAGE? = IMAGE(),
    @SerializedName("SMALL"     ) var SMALL     : IMAGE? = IMAGE(),
    @SerializedName("REGULAR"   ) var REGULAR   : IMAGE? = IMAGE()

)

data class IMAGE (

    @SerializedName("url"    ) var url    : String? = null,
    @SerializedName("width"  ) var width  : Int?    = null,
    @SerializedName("height" ) var height : Int?    = null

)
