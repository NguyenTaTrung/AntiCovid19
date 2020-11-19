package com.example.googlemap.di

import com.example.googlemap.data.resource.remote.utils.APICovid
import com.example.googlemap.data.resource.remote.utils.APIRss
import com.example.googlemap.data.resource.remote.utils.APIService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single(named("ApiService")) { get<Retrofit>(named("apiService")).create(APIService::class.java) }
    single(named("ApiCovid")) { get<Retrofit>(named("apiCovid")).create(APICovid::class.java) }
    single(named("ApiRss")) { get<Retrofit>(named("apiRss")).create(APIRss::class.java) }
}
