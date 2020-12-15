package com.example.googlemap.data.resource.repository

import com.example.googlemap.data.model.*
import com.example.googlemap.data.model.rss.Rss
import com.example.googlemap.data.resource.InformationDataSource
import com.example.googlemap.data.resource.local.entity.Information
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class InformationRepository(
    private val remote: InformationDataSource.Remote,
    private val local: InformationDataSource.Local
) : InformationDataSource.Remote,
    InformationDataSource.Local {

    override fun getInformation(): Observable<List<CaseInformation>> = remote.getInformation()

    override fun getAllPlace(id: Int): Observable<List<Place>> = remote.getAllPlace(id)

    override fun getDetailCase(id: Int): Observable<DetailCase> = remote.getDetailCase(id)

    override fun getSummaryData(): Observable<Summary> = remote.getSummaryData()

    override fun getCountryAllStatus(fromDate: String, toDate: String): Observable<List<CountryStatus>> =
        remote.getCountryAllStatus(fromDate, toDate)

    override fun getWorldNewsVNExpress(): Observable<Rss> = remote.getWorldNewsVNExpress()

    override fun getNewestNewsVNExpress(): Observable<Rss> = remote.getNewestNewsVNExpress()

    override fun getHighlightVNExpress(): Observable<Rss> = remote.getHighlightVNExpress()

    override fun getDomesticVNExpress(): Observable<Rss> = remote.getDomesticVNExpress()

    override fun getLanguage(): Boolean = local.getLanguage()

    override fun updateLanguage(isVietnamese: Boolean) {
        local.updateLanguage(isVietnamese)
    }

    override fun getLastInformation(): Flowable<Information> = local.getLastInformation()

    override fun informationCount(): Single<Int> = local.informationCount()

    override fun addInformation(information: Information) = local.addInformation(information)

    override fun updateInformation(information: Information) = local.updateInformation(information)
}
