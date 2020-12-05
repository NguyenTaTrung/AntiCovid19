package com.example.googlemap.ui.case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaseShareViewModel : ViewModel() {
    private val _place = MutableLiveData<String>()
    val place: LiveData<String>
        get() = _place

    private val _position = MutableLiveData<Int>()
    val position: LiveData<Int>
        get() = _position

    private val _infected = MutableLiveData<Boolean>()
    val infected: LiveData<Boolean>
        get() = _infected

    private val _dead = MutableLiveData<Boolean>()
    val dead: LiveData<Boolean>
        get() = _dead

    private val _recovered = MutableLiveData<Boolean>()
    val recovered: LiveData<Boolean>
        get() = _recovered

    fun setPlace(place: String) {
        _place.value = place
    }

    fun setPosition(pos: Int) {
        _position.value = pos
    }

    fun setInfected(value: Boolean) {
        _infected.value = value
    }

    fun setDead(value: Boolean) {
        _dead.value = value
    }

    fun setRecovered(value: Boolean) {
        _recovered.value = value
    }

    fun getInfected(): String = when (infected.value) {
        true -> "Đang điều trị"
        else -> ""
    }

    fun getDead(): String = when (dead.value) {
        true -> "Tử vong"
        else -> ""
    }

    fun getRecovered(): String = when (recovered.value) {
        true -> "Hồi phục"
        else -> ""
    }
}
