package com.example.googlemap.data.resource

import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.DetailCase
import com.example.googlemap.data.model.Summary
import com.example.googlemap.data.model.Symptom
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
        fun getWorldNewsVNExpress(): Observable<Rss>
        fun getNewestNewsVNExpress(): Observable<Rss>
        fun getHighlightVNExpress(): Observable<Rss>
        fun getDomesticVNExpress(): Observable<Rss>
    }
}
