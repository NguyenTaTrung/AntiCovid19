package com.example.googlemap.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.googlemap.R
import com.example.googlemap.base.BaseAdapter
import com.example.googlemap.data.model.Symptom

class HomeAdapter(private val onItemClick: (Symptom, Int) -> Unit) : BaseAdapter<Symptom, HomeViewHolder>(Symptom.diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recyclerview_home,
                parent,
                false
            ),
            onItemClick
        )
    }
}
