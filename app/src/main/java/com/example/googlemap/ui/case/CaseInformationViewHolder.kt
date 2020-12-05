package com.example.googlemap.ui.case

import com.example.googlemap.BR
import com.example.googlemap.base.BaseViewHolder
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.databinding.ItemRecyclerviewCaseInformationBinding

class CaseInformationViewHolder(
    private val binding: ItemRecyclerviewCaseInformationBinding,
    onItemClick: (CaseInformation, Int) -> Unit
) : BaseViewHolder<CaseInformation>(binding, onItemClick) {
    override val itemData: CaseInformation?
        get() = binding.caseInformation

    override fun bindData(item: CaseInformation) = with(binding) {
        setVariable(BR.caseInformation, item)
        executePendingBindings()
    }
}
