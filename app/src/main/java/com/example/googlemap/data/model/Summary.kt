package com.example.googlemap.data.model

import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("Message")
    val message: String,
    @SerializedName("Global")
    val global: Global,
    @SerializedName("Countries")
    val countries: List<Country>,
    @SerializedName("Date")
    val date: String
)
