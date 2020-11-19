package com.example.googlemap.data.model

import androidx.recyclerview.widget.DiffUtil

data class News(
    val title: String?,
    val link: String?,
    val time: String?,
    val imgUrl: String?
) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }
        }
    }
}
