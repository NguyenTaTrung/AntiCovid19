package com.example.googlemap.ui.case

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.googlemap.R
import com.example.googlemap.base.BaseAdapter
import com.example.googlemap.data.model.CaseInformation

class CaseInformationAdapter(
    private val onItemClick: (CaseInformation, Int) -> Unit
) : BaseAdapter<CaseInformation, CaseInformationViewHolder>(CaseInformation.diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseInformationViewHolder {
        return CaseInformationViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recyclerview_case_information,
                parent,
                false
            ), onItemClick
        )
    }
}
