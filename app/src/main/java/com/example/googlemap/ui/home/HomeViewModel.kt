package com.example.googlemap.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.resource.repository.InformationRepository

class HomeViewModel(private val repository: InformationRepository) : RxViewModel() {

    private val _isVietnameseLanguage = MutableLiveData<Boolean>()
    val isVietnameseLanguage: LiveData<Boolean>
        get() = _isVietnameseLanguage

    init {
        _isVietnameseLanguage.value = repository.getLanguage()
    }

    fun updateLanguage(isVietnamese: Boolean) {
        repository.updateLanguage(isVietnamese)
    }
}
