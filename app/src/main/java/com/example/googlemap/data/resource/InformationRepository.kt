package com.example.googlemap.data.resource

import com.example.googlemap.data.model.CaseInformation
import io.reactivex.rxjava3.core.Observable

class InformationRepository(
    private val remote: InformationDataSource.Remote
) : InformationDataSource.Remote {

    override fun getInformation(): Observable<List<CaseInformation>> = remote.getInformation()
}
