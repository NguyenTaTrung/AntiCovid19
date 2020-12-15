package com.example.googlemap.ui.information

import com.example.googlemap.BR
import com.example.googlemap.base.BaseViewHolder
import com.example.googlemap.data.model.Place
import com.example.googlemap.databinding.ItemRecyclerviewPlaceBinding

class PlaceViewHolder(
    private val binding: ItemRecyclerviewPlaceBinding,
    onItemClick: (Place, Int) -> Unit
) : BaseViewHolder<Place>(binding, onItemClick) {
    override val itemData: Place?
        get() = binding.place

    override fun bindData(item: Place) = with(binding) {
        setVariable(BR.place, item)
        executePendingBindings()
    }
}
