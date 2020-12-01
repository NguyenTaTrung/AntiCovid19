package com.example.googlemap.data.resource.remote.utils

import com.example.googlemap.data.model.CountryStatus
import com.example.googlemap.data.model.Summary
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface APICovid {
    @GET("summary")
    fun getSummaryData(): Observable<Summary>

    @GET("country/vietnam")
    fun getCountryAllStatus(
        @Query("from") fromDate: String,
        @Query("to") toDate: String
    ): Observable<List<CountryStatus>>
}
