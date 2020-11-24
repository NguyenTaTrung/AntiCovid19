package com.example.googlemap.ui.main

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.lifecycle.Observer
import androidx.navigation.ui.setupWithNavController
import com.example.googlemap.R
import com.example.googlemap.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        viewModel.isVietnameseLanguage.observe(this, Observer {
            setLanguage(it)

            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

            val navController = findNavController(R.id.fragmentContainer)
            bottomNavigationView.setupWithNavController(navController)
        })
    }

    private fun setLanguage(isVietnameseLanguage: Boolean) {
        val language = if (isVietnameseLanguage) "vi" else "en"
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}
