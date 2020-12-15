package com.example.googlemap.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class Place(
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
) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem == newItem
            }
        }
    }
}
