package com.example.googlemap.ui.case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.resource.repository.InformationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers.io

class CaseInformationViewModel(private val repository: InformationRepository) : RxViewModel() {
    private val listCase = mutableListOf<CaseInformation>()

    private val _caseInformation = MutableLiveData<List<CaseInformation>>()
    val caseInformation: LiveData<List<CaseInformation>>
        get() = _caseInformation

    init { getCaseInformation() }

    fun getCaseInformation() {
        listCase.clear()
        _isLoading.value = true
        repository.getInformation()
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _caseInformation.value = it
                listCase.addAll(it)
                _isLoading.value = false
            }, {
                _error.value = it.message.toString()
                _isLoading.value = false
            })
            .addTo(disposables)
    }

    fun searchFilter(place: String, infected: String, dead: String, recovered: String) {
        val newList = listCase.filter {
            it.detectingLocation == place
        }.filter {
            when (it.status) {
                infected, dead, recovered -> true
                else -> false
            }
        }
        _caseInformation.value = newList
    }
}
