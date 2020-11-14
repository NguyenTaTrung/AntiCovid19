package com.example.googlemap.data.model

import com.google.gson.annotations.SerializedName

data class CaseInformation(
    @SerializedName("id")
    var id: Int,
    @SerializedName("age")
    var age: Int,
    @SerializedName("address")
    var address: String,
    @SerializedName("sex")
    var sex: String,
    @SerializedName("detecting location")
    var detectingLocation: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("code")
    var code: String,
    @SerializedName("latitude")
    var lat: Double,
    @SerializedName("longitude")
    var lon: Double,
    @SerializedName("status")
    var status: String
)
