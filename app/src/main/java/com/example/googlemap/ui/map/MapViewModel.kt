package com.example.googlemap.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.resource.InformationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers

class MapViewModel(repository: InformationRepository) : RxViewModel() {
    private val _listInformation = MutableLiveData<List<CaseInformation>>()
    val listCaseInformation: LiveData<List<CaseInformation>>
        get() = _listInformation

    init {
        repository.getInformation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _listInformation.value = it
            }, {
                _error.value = it.message.toString()
            })
            .addTo(disposables)
    }
}
