package com.example.googlemap.data.resource.remote.utils

import com.example.googlemap.data.model.CaseInformation
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface APIService {
    @GET("connect.php")
    fun getInformation(): Observable<List<CaseInformation>>
}
