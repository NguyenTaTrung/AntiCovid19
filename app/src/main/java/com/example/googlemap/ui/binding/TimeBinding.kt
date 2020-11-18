package com.example.googlemap.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.googlemap.utils.TimeConst.ID_TIMEZONE
import com.example.googlemap.utils.TimeConst.INPUT_TIME_FORMAT
import com.example.googlemap.utils.TimeConst.OUTPUT_TIME_FORMAT
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("formatTime")
fun formatTime(textView: TextView, time: String?) {
    if (time != null) {
        val input = SimpleDateFormat(INPUT_TIME_FORMAT, Locale.getDefault())
        input.timeZone = TimeZone.getTimeZone(ID_TIMEZONE)

        val output = SimpleDateFormat(OUTPUT_TIME_FORMAT, Locale.getDefault())

        var date: Date? = null
        try {
            date = input.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        input.timeZone = TimeZone.getDefault()
        if (date != null) textView.text = output.format(date)
    }
}
