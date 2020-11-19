package com.example.googlemap.data.resource.remote

import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.Summary
import com.example.googlemap.data.model.rss.Rss
import com.example.googlemap.data.resource.InformationDataSource
import com.example.googlemap.data.resource.remote.utils.APICovid
import com.example.googlemap.data.resource.remote.utils.APIRss
import com.example.googlemap.data.resource.remote.utils.APIService
import io.reactivex.rxjava3.core.Observable

class InformationRemoteDataSource(
    private val apiService: APIService,
    private val apiCovid: APICovid,
    private val apiRss: APIRss
): InformationDataSource.Remote {

    override fun getInformation(): Observable<List<CaseInformation>> = apiService.getInformation()

    override fun getSummaryData(): Observable<Summary> = apiCovid.getSummaryData()

    override fun getWorldNewsVNExpress(): Observable<Rss> = apiRss.getWorldNewsVNExpress()

    override fun getNewestNewsVNExpress(): Observable<Rss> = apiRss.getNewestNewsVNExpress()

    override fun getHighlightVNExpress(): Observable<Rss> = apiRss.getHighlightVNExpress()

    override fun getDomesticVNExpress(): Observable<Rss> = apiRss.getDomesticVNExpress()
}
