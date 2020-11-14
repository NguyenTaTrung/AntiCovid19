package com.example.googlemap.di

import com.example.googlemap.data.resource.remote.utils.APIService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { get<Retrofit>().create(APIService::class.java) }
}
