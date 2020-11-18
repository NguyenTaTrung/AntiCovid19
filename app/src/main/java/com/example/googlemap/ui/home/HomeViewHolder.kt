package com.example.googlemap.ui.home

import com.example.googlemap.BR
import com.example.googlemap.base.BaseViewHolder
import com.example.googlemap.data.model.Symptom
import com.example.googlemap.databinding.ItemRecyclerviewHomeBinding

class HomeViewHolder(
    private val binding: ItemRecyclerviewHomeBinding,
    onItemClick: (Symptom, Int) -> Unit
) : BaseViewHolder<Symptom>(binding, onItemClick) {
    override val itemData: Symptom?
        get() = binding.symptom

    override fun bindData(item: Symptom) = with(binding) {
        setVariable(BR.symptom, item)
        executePendingBindings()
    }
}
