package com.example.googlemap.utils

import android.widget.TextView

fun TextView.setLeftDrawable(drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(drawable,0,0,0)
}

fun TextView.hideDrawable() {
    setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
}
