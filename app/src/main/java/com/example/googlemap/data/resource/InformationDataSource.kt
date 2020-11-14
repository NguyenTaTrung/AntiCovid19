package com.example.googlemap.data.resource

import com.example.googlemap.data.model.CaseInformation
import io.reactivex.rxjava3.core.Observable

interface InformationDataSource {
    interface Remote {
        fun getInformation(): Observable<List<CaseInformation>>
    }
}
