package com.example.googlemap.data.model

import com.google.gson.annotations.SerializedName

class DetailCase(
    @SerializedName("id_detail")
    var idDetail: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("time")
    var time: String,
    @SerializedName("latitude")
    var lat: Double,
    @SerializedName("longitude")
    var lon: Double,
    @SerializedName("id")
    var id: Int,
    @SerializedName("address")
    var address: String
)
