package com.example.googlemap.ui.detail

import com.example.googlemap.BR
import com.example.googlemap.base.BaseViewHolder
import com.example.googlemap.data.model.Country
import com.example.googlemap.databinding.ItemRecyclerviewCountriesBinding

class DetailCountryViewHolder(
    private val binding: ItemRecyclerviewCountriesBinding,
    onItemClick: (Country, Int) -> Unit
) : BaseViewHolder<Country>(binding, onItemClick) {
    override val itemData: Country?
        get() = binding.country

    override fun bindData(item: Country) = with(binding) {
        setVariable(BR.country, item)
        executePendingBindings()
    }
}
