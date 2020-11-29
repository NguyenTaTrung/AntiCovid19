package com.example.googlemap.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.DetailCase
import com.example.googlemap.data.resource.InformationRepository
import com.example.googlemap.data.resource.LocationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers.io

class MapViewModel(
    private val repository: InformationRepository,
    private val locationRepository: LocationRepository
) : RxViewModel() {
    val receivingLocationUpdates: LiveData<Boolean> = locationRepository.receivingLocationUpdates

    private val _listInformation = MutableLiveData<List<CaseInformation>>()
    val listCaseInformation: LiveData<List<CaseInformation>>
        get() = _listInformation

    private val _listDetailInformation = MutableLiveData<List<DetailCase>>()
    val listDetailInformation: LiveData<List<DetailCase>>
        get() = _listDetailInformation

    init {
        repository.getInformation()
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _listInformation.value = it
            }, {
                _error.value = it.message.toString()
            })
            .addTo(disposables)
    }

    fun getListDetailInformation(id: Int) {
        repository.getDetailInformation(id)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _listDetailInformation.value = it
            }, {
                _error.value = it.message.toString()
            })
            .addTo(disposables)
    }

    fun startLocationUpdates() = locationRepository.startLocationUpdates()

    fun stopLocationUpdates() = locationRepository.stopLocationUpdates()
}
