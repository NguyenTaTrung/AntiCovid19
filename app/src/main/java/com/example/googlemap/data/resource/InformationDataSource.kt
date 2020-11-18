package com.example.googlemap.data.resource

import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.Summary
import com.example.googlemap.data.model.Symptom
import io.reactivex.rxjava3.core.Observable

interface InformationDataSource {
    interface Local {
        fun getSymptoms(): List<Symptom>
    }

    interface Remote {
        fun getInformation(): Observable<List<CaseInformation>>
        fun getSummaryData(): Observable<Summary>
    }
}
