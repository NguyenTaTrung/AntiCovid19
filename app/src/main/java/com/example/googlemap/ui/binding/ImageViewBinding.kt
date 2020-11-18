package com.example.googlemap.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageResource")
fun loadImageResource(imageView: ImageView, id: Int) {
    imageView.setBackgroundResource(id)
}
