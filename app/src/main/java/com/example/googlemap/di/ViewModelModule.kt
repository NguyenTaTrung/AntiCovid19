package com.example.googlemap.di

import com.example.googlemap.ui.detail.DetailCountryViewModel
import com.example.googlemap.ui.home.HomeViewModel
import com.example.googlemap.ui.main.MainViewModel
//import com.example.googlemap.ui.main.MainViewModel
import com.example.googlemap.ui.map.MapViewModel
import com.example.googlemap.ui.news.domestic.DomesticNewsViewModel
import com.example.googlemap.ui.news.hightlight.HighlightNewsViewModel
import com.example.googlemap.ui.news.newest.NewestNewsViewModel
import com.example.googlemap.ui.news.world.WorldNewsViewModel
import com.example.googlemap.ui.statistic.StatisticViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { StatisticViewModel(get()) }
    viewModel { DetailCountryViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { HighlightNewsViewModel(get()) }
    viewModel { NewestNewsViewModel(get()) }
    viewModel { WorldNewsViewModel(get()) }
    viewModel { DomesticNewsViewModel(get()) }
}
