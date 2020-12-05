package com.example.googlemap.ui.dialog.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.resource.repository.InformationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers.io

class SearchFilterViewModel(repository: InformationRepository) : RxViewModel() {
    private val _spinnerArray = MutableLiveData<List<String>>()
    val spinnerArray: LiveData<List<String>>
        get() = _spinnerArray

    init {
        repository.getInformation()
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { Observable.fromIterable(it) }
            .map { item -> item.detectingLocation }
            .distinct()
            .toSortedList()
            .subscribe({
                _spinnerArray.value = it
            }, {
                _error.value = it.message.toString()
            })
            .addTo(disposables)
    }
}
