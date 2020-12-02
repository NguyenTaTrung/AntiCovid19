package com.example.googlemap.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.googlemap.data.resource.repository.InformationRepository

class MainViewModel(repository: InformationRepository) : ViewModel() {

    private val _isVietnameseLanguage = MutableLiveData<Boolean>()
    val isVietnameseLanguage: LiveData<Boolean>
        get() = _isVietnameseLanguage

    init {
        _isVietnameseLanguage.value = repository.getLanguage()
    }
}
