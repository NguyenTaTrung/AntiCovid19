package com.example.googlemap.di

import com.example.googlemap.utils.LinkConst.COVID_API
import com.example.googlemap.utils.LinkConst.VNEXPRESS_RSS
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

val networkModule = module {
    single(named("apiService")) {
        Retrofit.Builder()
//            .baseUrl("http://192.168.11.7/MyServer/")
            .baseUrl("http://192.168.137.1/MyServer/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single(named("apiCovid")) {
        Retrofit.Builder()
            .baseUrl(COVID_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single(named("apiRss")) {
        Retrofit.Builder()
            .baseUrl(VNEXPRESS_RSS)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}
