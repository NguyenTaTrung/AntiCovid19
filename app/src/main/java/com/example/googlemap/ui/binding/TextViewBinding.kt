package com.example.googlemap.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.googlemap.R

@BindingAdapter("statusColor")
fun statusColor(textView: TextView, status: String?) {
    status?.let {
        when(it) {
            "Đang điều trị" -> textView.setTextColor(textView.resources.getColor(R.color.colorOrange, textView.context.theme))
            "Tử vong" -> textView.setTextColor(textView.resources.getColor(R.color.colorRed, textView.context.theme))
            "Hồi phục" -> textView.setTextColor(textView.resources.getColor(R.color.colorGreen, textView.context.theme))
        }
    }
}
