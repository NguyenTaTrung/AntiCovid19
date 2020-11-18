package com.example.googlemap.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.base.RxViewModel
import com.example.googlemap.data.model.Symptom
import com.example.googlemap.data.resource.InformationRepository

class HomeViewModel(repository: InformationRepository): RxViewModel() {

    private val _symptoms = MutableLiveData<List<Symptom>>()
    val symptoms: LiveData<List<Symptom>>
        get() = _symptoms

    init {
        _symptoms.value = repository.getSymptoms()
    }
}
