package com.example.googlemap.ui.statistic

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.R
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.Country
import com.example.googlemap.data.model.CountryStatus
import com.example.googlemap.data.model.Global
import com.example.googlemap.data.model.Summary
import com.example.googlemap.data.resource.InformationRepository
import com.example.googlemap.utils.TimeConst.PATTERN_TIME_EN
import com.example.googlemap.utils.TimeConst.PATTERN_TIME_VN
import com.example.googlemap.utils.TimeConst.TIME_ZONE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers.io
import java.text.SimpleDateFormat
import java.util.*

class StatisticViewModel(
    private val repository: InformationRepository,
    private val application: Application
) : RxViewModel() {

    private val _summary = MutableLiveData<Summary>()
    val summary: LiveData<Summary>
        get() = _summary

    private val _vietNamInformation = MutableLiveData<Country>()
    val vietNamInformation: LiveData<Country>
        get() = _vietNamInformation

    private val _globalInformation = MutableLiveData<Global>()
    val globalInformation: LiveData<Global>
        get() = _globalInformation

    private val _countryStatus = MutableLiveData<List<CountryStatus>>()
    val countryStatus: LiveData<List<CountryStatus>>
        get() = _countryStatus

    private val _isVietNam = MutableLiveData<Boolean>()
    val isVietNam: LiveData<Boolean>
        get() = _isVietNam

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    init {
        _isLoading.value = true
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
        }
        getCountryAllStatus(calendar.timeInMillis, Calendar.getInstance().timeInMillis, true)
        repository.getSummaryData()
            .subscribeOn(io())
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

    fun getCountryAllStatus(fromTime: Long?, toTime: Long?, dayOfWeek: Boolean) {
        val inputTimeEn = SimpleDateFormat(PATTERN_TIME_EN, Locale.getDefault())
        val inputTimeVn = SimpleDateFormat(PATTERN_TIME_VN, Locale.getDefault())

        val fromDate = inputTimeVn.format(fromTime)
        val toDate = inputTimeVn.format(toTime)

        if (fromDate == getCurrentDay() && toDate == getCurrentDay()) {
            _message.value = getApplicationString(R.string.msg_no_data_available)
            if (!dayOfWeek) _error.value = getApplicationString(R.string.msg_no_data_available) + toDate
            return
        } else if (fromDate == toDate) {
            _message.value = getApplicationString(R.string.text_date_time) + toDate
        } else {
            _message.value = getApplicationString(R.string.text_from_date) + fromDate +
                    getApplicationString(R.string.text_to_date) + toDate
        }

        if (dayOfWeek) {
            getAllStatus(
                inputTimeEn.format(fromTime?.minus(86400000)) + TIME_ZONE,
                inputTimeEn.format(toTime?.minus(86400000)) + TIME_ZONE,
            )
        } else {
            getAllStatus(
                inputTimeEn.format(fromTime?.minus(86400000)) + TIME_ZONE,
                inputTimeEn.format(toTime) + TIME_ZONE,
            )
        }
    }

    private fun getAllStatus(fromDate: String, toDate: String) {
        repository.getCountryAllStatus(fromDate, toDate)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.size > 1) {
                    _countryStatus.value = it
                } else {
                    _message.value = getApplicationString(R.string.msg_no_data_available)
                }
            }, {
                _error.value = it.message.toString()
            })
            .addTo(disposables)
    }

    private fun getApplicationString(id: Int): String = application.getString(id)

    private fun getCurrentDay(): String {
        val input = SimpleDateFormat(PATTERN_TIME_VN, Locale.getDefault())
        return input.format(Calendar.getInstance().timeInMillis)
    }
}
