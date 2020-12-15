package com.example.googlemap.ui.information

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.googlemap.R
import com.example.googlemap.databinding.ActivityDetailCaseBinding

class DetailCaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityDetailCaseBinding>(this, R.layout.activity_detail_case)


    }
}
