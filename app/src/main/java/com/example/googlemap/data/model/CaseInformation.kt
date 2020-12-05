package com.example.googlemap.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
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
    var status: String,
    @SerializedName("time")
    var time: String
) : Parcelable {

    constructor(jsonObject: JSONObject) : this(
        jsonObject.getInt(ID_CASE),
        jsonObject.getInt(AGE),
        jsonObject.getString(ADDRESS),
        jsonObject.getString(SEX),
        jsonObject.getString(DETECTING_LOCATION),
        jsonObject.getString(DESCRIPTION),
        jsonObject.getString(CODE),
        jsonObject.getDouble(LATITUDE),
        jsonObject.getDouble(LONGITUDE),
        jsonObject.getString(STATUS),
        jsonObject.getString(TIME)
    )

    override fun toString(): String {
        return "{id=" + id + ", " +
                "age=" + age + ", " +
                "address='" + address + "', " +
                "sex='" + sex + "', " +
                "detectingLocation='" + detectingLocation + "', " +
                "description='" + description + "', " +
                "code='" + code + "', " +
                "lat=" + lat + ", " +
                "lon=" + lon + ", " +
                "status='" + status + "', " +
                "time='" + time + "'}"
    }

    companion object {
        private const val ID_CASE = "id"
        private const val AGE = "age"
        private const val ADDRESS = "address"
        private const val SEX = "sex"
        private const val DETECTING_LOCATION = "detectingLocation"
        private const val DESCRIPTION = "description"
        private const val CODE = "code"
        private const val LATITUDE = "lat"
        private const val LONGITUDE = "lon"
        private const val STATUS = "status"
        private const val TIME = "time"

        val diffUtil = object : DiffUtil.ItemCallback<CaseInformation>() {
            override fun areItemsTheSame(
                oldItem: CaseInformation,
                newItem: CaseInformation
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CaseInformation,
                newItem: CaseInformation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
