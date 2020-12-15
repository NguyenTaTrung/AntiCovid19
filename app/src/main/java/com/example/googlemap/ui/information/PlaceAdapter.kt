package com.example.googlemap.ui.information

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.googlemap.R
import com.example.googlemap.base.BaseAdapter
import com.example.googlemap.data.model.Place

class PlaceAdapter(
    private val onItemClick: (Place, Int) -> Unit
) : BaseAdapter<Place, PlaceViewHolder>(Place.diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recyclerview_place,
                parent,
                false
            ), onItemClick
        )
    }
}
