package com.example.googlemap.data.resource.remote.utils

import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.DetailCase
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @GET("connect.php")
    fun getInformation(): Observable<List<CaseInformation>>

    @FormUrlEncoded
    @POST("detail.php")
    fun getDetailInformation(@Field("id") id: Int): Observable<List<DetailCase>>
}
