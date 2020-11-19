package com.example.googlemap.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.googlemap.R
import com.example.googlemap.base.BaseAdapter
import com.example.googlemap.data.model.Country

class DetailCountryAdapter(
    private val onItemClick: (Country, Int) -> Unit,
    private val listChange: () -> Unit
) : BaseAdapter<Country, DetailCountryViewHolder>(Country.diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailCountryViewHolder {
        return DetailCountryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recyclerview_countries,
                parent,
                false
            ), onItemClick
        )
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Country>,
        currentList: MutableList<Country>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        listChange()
    }
}
