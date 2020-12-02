package com.example.googlemap.data.resource

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.broadcast.LocationUpdatesBroadcastReceiver
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.resource.repository.InformationRepository
import com.example.googlemap.utils.hasPermission
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers.io
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

class MyLocationManager(
    private val context: Context,
    private val informationRepository: InformationRepository
) {
    private val listCaseInformation = mutableListOf<CaseInformation?>()
    private var disposable: Disposable? = null

    private val _receivingLocationUpdates = MutableLiveData<Boolean>(false)
    val receivingLocationUpdates: LiveData<Boolean>
        get() = _receivingLocationUpdates

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest().apply {
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationUpdatePendingIntent by lazy {
        val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java)
        intent.data = Uri.parse(listCaseInformation.toString())
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @Throws(SecurityException::class)
    fun startLocationUpdates() {
        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) return

        try {
            disposable = informationRepository.getInformation()
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listCaseInformation.addAll(it)
                }, {
                    it.printStackTrace()
                }, {
                    _receivingLocationUpdates.value = true
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationUpdatePendingIntent)
                    disposable?.dispose()
                })
        } catch (permissionRevoked: SecurityException) {
            _receivingLocationUpdates.value = false
            Log.d("TAG", "Location permission revoked; details: $permissionRevoked")
            throw permissionRevoked
        }
    }

    fun stopLocationUpdates() {
        Log.d("TAG", "stopLocationUpdates()")
        _receivingLocationUpdates.value = false
        fusedLocationClient.removeLocationUpdates(locationUpdatePendingIntent)
    }
}
