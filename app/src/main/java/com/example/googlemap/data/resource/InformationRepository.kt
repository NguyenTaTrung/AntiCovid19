package com.example.googlemap.data.resource

import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.Summary
import com.example.googlemap.data.model.Symptom
import io.reactivex.rxjava3.core.Observable

class InformationRepository(
    private val remote: InformationDataSource.Remote,
    private val local: InformationDataSource.Local
) : InformationDataSource.Remote, InformationDataSource.Local {

    override fun getInformation(): Observable<List<CaseInformation>> = remote.getInformation()

    override fun getSummaryData(): Observable<Summary> = remote.getSummaryData()

    override fun getSymptoms(): List<Symptom> = local.getSymptoms()
}
