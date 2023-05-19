package com.example.assignment7.data_class

import com.google.gson.annotations.SerializedName

data class Link (
    @SerializedName("from"   ) var from  : Int?            = null,
    @SerializedName("to"     ) var to    : Int?            = null,
    @SerializedName("count"  ) var count : Int?            = null,
    @SerializedName("hits"   ) var hits  : ArrayList<Hits> = arrayListOf()
)

data class Hits (
    @SerializedName("_links" ) var Links  : Links?  = Links()
)

data class Links (
    @SerializedName("self" ) var self : Self? = Self()
)


data class Self (
    @SerializedName("href"  ) var href  : String? = null,
)