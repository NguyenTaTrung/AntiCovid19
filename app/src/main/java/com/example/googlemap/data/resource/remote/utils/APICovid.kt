package com.example.googlemap.data.resource.remote.utils

import com.example.googlemap.data.model.Summary
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface APICovid {
    @GET("summary")
    fun getSummaryData(): Observable<Summary>
}
