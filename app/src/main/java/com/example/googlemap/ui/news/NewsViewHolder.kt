package com.example.googlemap.ui.news

import com.example.googlemap.BR
import com.example.googlemap.base.BaseViewHolder
import com.example.googlemap.data.model.News
import com.example.googlemap.databinding.ItemRecyclerviewNewsBinding

class NewsViewHolder(
    private val binding: ItemRecyclerviewNewsBinding,
    onItemClick: (News, Int) -> Unit
) : BaseViewHolder<News>(binding, onItemClick) {
    override val itemData: News?
        get() = binding.news

    override fun bindData(item: News) = with(binding) {
        setVariable(BR.news, item)
        executePendingBindings()
    }
}
