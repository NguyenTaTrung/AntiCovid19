package com.example.googlemap.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.Country
import com.example.googlemap.data.resource.InformationRepository
import com.example.googlemap.utils.ModelConst.TOTAL_CONFIRMED
import com.example.googlemap.utils.ModelConst.TOTAL_DEATHS
import com.example.googlemap.utils.ModelConst.TOTAL_RECOVERED
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers.io
import java.util.*

class DetailCountryViewModel(private val repository: InformationRepository) : RxViewModel() {

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>>
        get() = _countries

    init {
        _isLoading.value = true
        getDataApi()
    }

    fun getDataApi() {
        repository.getSummaryData()
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _countries.value = it.countries
                _isLoading.value = false
            }, {
                _error.value = it.message.toString()
                _isLoading.value = false
            })
            .addTo(disposables)
    }

    fun searchCountries(value: String) {
        _isLoading.value = true
        repository.getSummaryData()
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val newCountries = it.countries.filter { country ->
                    country.slug.contains(value.toLowerCase(Locale.getDefault()))
                }
                if (newCountries.isNullOrEmpty()) {
                    _error.value = null
                } else {
                    _countries.value = newCountries
                }
                _isLoading.value = false
            }, {
                _error.value = it.message.toString()
                _isLoading.value = false
            })
            .addTo(disposables)
    }

    fun sortCountries(key: String, isDescending: Boolean) {
        if (isDescending) {
            when (key) {
                TOTAL_CONFIRMED -> _countries.value = _countries.value?.sortedByDescending { it.totalConfirmed }
                TOTAL_DEATHS -> _countries.value = _countries.value?.sortedByDescending { it.totalDeaths }
                TOTAL_RECOVERED -> _countries.value = _countries.value?.sortedByDescending { it.totalRecovered }
            }
        } else {
            when (key) {
                TOTAL_CONFIRMED -> _countries.value = _countries.value?.sortedBy { it.totalConfirmed }
                TOTAL_DEATHS -> _countries.value = _countries.value?.sortedBy { it.totalDeaths }
                TOTAL_RECOVERED -> _countries.value = _countries.value?.sortedBy { it.totalRecovered }
            }
        }
    }
}
