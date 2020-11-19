package com.example.googlemap.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.googlemap.R
import com.example.googlemap.base.BaseAdapter
import com.example.googlemap.data.model.News

class NewsAdapter(private val onItemClick: (News, Int) -> Unit) : BaseAdapter<News, NewsViewHolder>(News.diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recyclerview_news,
                parent,
                false
            ), onItemClick
        )
    }
}
