package com.example.googlemap.ui.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.Country
import com.example.googlemap.data.model.Global
import com.example.googlemap.data.model.Summary
import com.example.googlemap.data.resource.InformationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class StatisticViewModel(repository: InformationRepository) : RxViewModel() {

    private val _summary = MutableLiveData<Summary>()
    val summary: LiveData<Summary>
        get() = _summary

    private val _vietNamInformation = MutableLiveData<Country>()
    val vietNamInformation: LiveData<Country>
        get() = _vietNamInformation

    private val _globalInformation = MutableLiveData<Global>()
    val globalInformation: LiveData<Global>
        get() = _globalInformation

    private val _isVietNam = MutableLiveData<Boolean>()
    val isVietNam: LiveData<Boolean>
        get() = _isVietNam

    init {
        _isLoading.value = true
        repository.getSummaryData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _summary.value = it
                getVietNamInformation()
                _isLoading.value = false
            }, {
                _error.value = it.message.toString()
                _isLoading.value = false
            })
            .addTo(disposables)
    }

    fun getVietNamInformation() {
        summary.value?.countries?.firstOrNull {
            it.country == "Viet Nam"
        }?.let { country ->
            _vietNamInformation.value = country
        }
        _isVietNam.value = true
    }

    fun getGlobalInformation() {
        _isVietNam.value = false
        _globalInformation.value = summary.value?.global
    }
}
