package com.example.googlemap.data.resource.remote.utils

import com.example.googlemap.data.model.rss.Rss
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface APIRss {
    @GET("the-gioi.rss")
    fun getWorldNewsVNExpress(): Observable<Rss>

    @GET("tin-moi-nhat.rss")
    fun getNewestNewsVNExpress(): Observable<Rss>

    @GET("tin-noi-bat.rss")
    fun getHighlightVNExpress(): Observable<Rss>

    @GET("thoi-su.rss")
    fun getDomesticVNExpress(): Observable<Rss>
}
