package com.example.googlemap.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Symptom(
    var image: Int,
    var title: String,
    var content: String
) : Parcelable {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Symptom>() {
            override fun areItemsTheSame(oldItem: Symptom, newItem: Symptom): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: Symptom, newItem: Symptom): Boolean {
                return oldItem == newItem
            }
        }
    }
}
