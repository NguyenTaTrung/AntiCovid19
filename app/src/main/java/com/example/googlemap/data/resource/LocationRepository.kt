package com.example.googlemap.data.resource

import androidx.lifecycle.LiveData

class LocationRepository(private val myLocationManager: MyLocationManager) {
    val receivingLocationUpdates: LiveData<Boolean> = myLocationManager.receivingLocationUpdates

    fun startLocationUpdates() = myLocationManager.startLocationUpdates()

    fun stopLocationUpdates() = myLocationManager.stopLocationUpdates()
}
