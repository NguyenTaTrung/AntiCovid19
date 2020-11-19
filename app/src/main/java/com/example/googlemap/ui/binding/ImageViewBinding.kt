package com.example.googlemap.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("imageResource")
fun loadImageResource(imageView: ImageView, id: Int) {
    imageView.setBackgroundResource(id)
}

@BindingAdapter("imageInternet")
fun loadImageInternet(imageView: ShapeableImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .into(imageView)
}
