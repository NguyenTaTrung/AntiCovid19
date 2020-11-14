package com.example.googlemap.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.googlemap.R
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private var client: FusedLocationProviderClient? = null
    private var mapFragment: SupportMapFragment? = null
    private var marker: Marker? = null
    private var binding: ActivityMapsBinding? = null
    private var markerMap = mutableMapOf<Marker?, CaseInformation>()
    private val viewModel by viewModel<MapViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()
        observeData()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_NEED_MAP -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getCurrentLocation()
                }
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val information = markerMap[marker]
        if (marker != this.marker) {
            cardInformation.visibility = View.VISIBLE
            binding?.caseInformation = information
        } else {
            cardInformation.visibility = View.GONE
        }
        return false
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun observeData() {
        viewModel.listCaseInformation.observe(this, Observer {
            addMoreLocation(it)
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_NEED_MAP
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        client?.lastLocation?.addOnSuccessListener {
            it?.let { location ->
                mapFragment?.getMapAsync { map ->
                    val sydney = LatLng(location.latitude, location.longitude)
                    marker = map?.addMarker(MarkerOptions().position(sydney).title("My"))
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16F))
                }
            }
        }
    }

    private fun addMoreLocation(data: List<CaseInformation>) {
        for (i in data.indices) {
            mapFragment?.getMapAsync { map ->
                val sydney = LatLng(data[i].lat, data[i].lon)
                val marker = map?.addMarker(
                    MarkerOptions().position(sydney).icon(bitmapDescriptorFromVector(this))
                )
                markerMap[marker] = data[i]
                map?.setOnMarkerClickListener(this)
            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, R.drawable.ic_warning)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    companion object {
        private const val REQUEST_CODE_NEED_MAP = 40
    }
}
