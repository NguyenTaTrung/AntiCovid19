package com.example.googlemap.data.resource.repository

import androidx.lifecycle.LiveData
import com.example.googlemap.data.resource.MyLocationManager

class LocationRepository(private val myLocationManager: MyLocationManager) {
    val receivingLocationUpdates: LiveData<Boolean> = myLocationManager.receivingLocationUpdates

    fun startLocationUpdates() = myLocationManager.startLocationUpdates()

    fun stopLocationUpdates() = myLocationManager.stopLocationUpdates()
}
