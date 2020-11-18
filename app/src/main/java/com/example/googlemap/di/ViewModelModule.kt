package com.example.googlemap.di

import com.example.googlemap.ui.home.HomeViewModel
import com.example.googlemap.ui.statistic.StatisticViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { StatisticViewModel(get()) }
}
