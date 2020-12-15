package com.example.googlemap.data.resource

import com.example.googlemap.data.model.*
import com.example.googlemap.data.model.rss.Rss
import com.example.googlemap.data.resource.local.entity.Information
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface InformationDataSource {
    interface Local {
        fun getLanguage(): Boolean
        fun updateLanguage(isVietnamese: Boolean)
        fun getLastInformation(): Flowable<Information>
        fun informationCount(): Single<Int>
        fun addInformation(information: Information): Completable
        fun updateInformation(information: Information): Completable
    }

    interface Remote {
        fun getInformation(): Observable<List<CaseInformation>>
        fun getAllPlace(id: Int): Observable<List<Place>>
        fun getDetailCase(id: Int): Observable<DetailCase>
        fun getSummaryData(): Observable<Summary>
        fun getCountryAllStatus(fromDate: String, toDate: String): Observable<List<CountryStatus>>
        fun getWorldNewsVNExpress(): Observable<Rss>
        fun getNewestNewsVNExpress(): Observable<Rss>
        fun getHighlightVNExpress(): Observable<Rss>
        fun getDomesticVNExpress(): Observable<Rss>
    }
}
