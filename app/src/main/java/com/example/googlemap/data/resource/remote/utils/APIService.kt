package com.example.googlemap.data.resource.remote.utils

import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.DetailCase
import com.example.googlemap.data.model.Place
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @GET("information.php")
    fun getInformation(): Observable<List<CaseInformation>>

    @FormUrlEncoded
    @POST("place.php")
    fun getAllPlace(@Field("id") id: Int): Observable<List<Place>>

    @FormUrlEncoded
    @POST("detailCase.php")
    fun getDetailCase(@Field("id") id: Int): Observable<DetailCase>
}
