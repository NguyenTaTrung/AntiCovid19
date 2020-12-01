package com.example.googlemap.data.resource

import com.example.googlemap.data.model.*
import com.example.googlemap.data.model.rss.Rss
import io.reactivex.rxjava3.core.Observable

interface InformationDataSource {
    interface Local {
        fun getLanguage(): Boolean
        fun updateLanguage(isVietnamese: Boolean)
    }

    interface Remote {
        fun getInformation(): Observable<List<CaseInformation>>
        fun getDetailInformation(id: Int): Observable<List<DetailCase>>
        fun getSummaryData(): Observable<Summary>
        fun getCountryAllStatus(fromDate: String, toDate: String): Observable<List<CountryStatus>>
        fun getWorldNewsVNExpress(): Observable<Rss>
        fun getNewestNewsVNExpress(): Observable<Rss>
        fun getHighlightVNExpress(): Observable<Rss>
        fun getDomesticVNExpress(): Observable<Rss>
    }
}
