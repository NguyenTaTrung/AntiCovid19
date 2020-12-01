package com.example.googlemap.data.model

import com.google.gson.annotations.SerializedName

data class CountryStatus(
    @SerializedName("Country")
    val country: String,
    @SerializedName("CountryCode")
    val code: String,
    @SerializedName("Province")
    val province: String,
    @SerializedName("City")
    val city: String,
    @SerializedName("CityCode")
    val cityCode: String,
    @SerializedName("Lat")
    val latitude: String,
    @SerializedName("Lon")
    val longitude: String,
    @SerializedName("Confirmed")
    val confirmed: Int,
    @SerializedName("Deaths")
    val deaths: Int,
    @SerializedName("Recovered")
    val recovered: Int,
    @SerializedName("Active")
    val active: Int,
    @SerializedName("Date")
    val date: String
)
