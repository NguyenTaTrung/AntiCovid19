package com.example.googlemap.ui.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.DetailCase
import com.example.googlemap.data.resource.repository.InformationRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers.io

class DetailCaseInformationViewModel(private val repository: InformationRepository) : RxViewModel() {

    private val _detailCase = MutableLiveData<DetailCase>()
    val detailCase: LiveData<DetailCase>
        get() = _detailCase

    fun getDetailCase(id: Int) {
        repository.getDetailCase(id)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _detailCase.value = it
            }, {
                _error.value = it.message.toString()
            })
            .addTo(disposables)
    }
}
