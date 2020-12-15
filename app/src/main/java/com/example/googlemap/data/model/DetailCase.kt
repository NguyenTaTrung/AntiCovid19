package com.example.googlemap.data.model

import com.google.gson.annotations.SerializedName

data class DetailCase(
    @SerializedName("information")
    val information: CaseInformation,
    @SerializedName("detailCase")
    val detailPlace: List<Place>
)
