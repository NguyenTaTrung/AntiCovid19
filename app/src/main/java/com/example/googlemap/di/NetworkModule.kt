package com.example.googlemap.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single(named("apiService")) {
        Retrofit.Builder()
            .baseUrl("http://192.168.11.8/MyServer/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single(named("apiCovid")) {
        Retrofit.Builder()
            .baseUrl("https://api.covid19api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}
