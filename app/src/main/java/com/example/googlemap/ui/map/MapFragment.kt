package com.example.googlemap.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.DetailCase
import com.example.googlemap.databinding.FragmentMapBinding
import com.example.googlemap.ui.dialog.BackgroundPermissionDialog
import com.example.googlemap.ui.dialog.PermissionRequestType
import com.example.googlemap.utils.hasPermission
import com.example.googlemap.utils.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment :
    BaseFragment<FragmentMapBinding>(),
    GoogleMap.OnMarkerClickListener,
    BackgroundPermissionDialog.BackgroundDialogCallbacks
{

    private var client: FusedLocationProviderClient? = null
    private var mapFragment: SupportMapFragment? = null
    private var marker: Marker? = null
    private var markers = mutableListOf<Marker?>()
    private var newMarkers = mutableListOf<Marker?>()
    private var markerMap = mutableMapOf<Marker?, CaseInformation>()
    private var newMarkerMap = mutableMapOf<Marker?, DetailCase>()
    private val viewModel by viewModel<MapViewModel>()
    private var canBackPress = true

    override val layoutResources: Int
        get() = R.layout.fragment_map

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (!requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            BackgroundPermissionDialog.newInstance(PermissionRequestType.FINE_LOCATION).show(childFragmentManager, null)
        } else {
            getCurrentLocation()
            viewModel.startLocationUpdates()
        }
        observeData()
    }

    override fun initAction() {
//        imageButtonRefresh.setOnClickListener { getCurrentLocation() }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!canBackPress) {
                        markers.forEach {
                            it?.isVisible = true
                        }
                        newMarkers.forEach {
                            it?.isVisible = false
                        }
                        cardInformationDetail.visibility = View.GONE
                        cardInformation.visibility = View.VISIBLE
                        newMarkers.clear()
                        canBackPress = true
                        return
                    }
                    findNavController().popBackStack()
                }
            })
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.tag?.equals("a") == true) {
            val information = markerMap[marker]
            if (marker != this.marker) {
                cardInformation.visibility = View.VISIBLE
                binding.caseInformation = information
                markers.forEach {
                    it?.isVisible = false
                }
                information?.id?.let { viewModel.getListDetailInformation(it) }
            } else {
                cardInformation.visibility = View.GONE
                cardInformationDetail.visibility = View.GONE
            }
        } else {
            if (marker != this.marker) {
                cardInformation.visibility = View.GONE
                cardInformationDetail.visibility = View.VISIBLE
                binding.detailCase = newMarkerMap[marker]
            } else {
                cardInformation.visibility = View.GONE
                cardInformationDetail.visibility = View.GONE
            }
        }
        return false
    }

    override fun removeFragment() { findNavController().popBackStack() }

    override fun refreshFragment() {
        getCurrentLocation()
        viewModel.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.receivingLocationUpdates.value == true &&
            (!requireContext().hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
        ) {
            viewModel.stopLocationUpdates()
        }
    }

    private fun observeData() = with(viewModel) {
        listCaseInformation.observe(viewLifecycleOwner, Observer {
            addMoreLocation(it)
        })

        listDetailInformation.observe(viewLifecycleOwner, Observer {
            canBackPress = false
            addNewLocation(it)
        })

        error.observe(viewLifecycleOwner, Observer {
            context?.showToast(it)
        })
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        client?.lastLocation?.addOnSuccessListener {
            it?.let { location ->
                mapFragment?.getMapAsync { map ->
                    val sydney = LatLng(location.latitude, location.longitude)
                    marker = map?.addMarker(MarkerOptions().position(sydney).title(""))
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
                    MarkerOptions().position(sydney)
                        .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_warning))
                )
                marker?.tag = "a"
                markers.add(marker)
                markerMap[marker] = data[i]
                map?.setOnMarkerClickListener(this)
            }
        }
    }

    private fun addNewLocation(data: List<DetailCase>) {
        for (i in data.indices) {
            mapFragment?.getMapAsync { map ->
                val sydney = LatLng(data[i].lat, data[i].lon)
                val marker = map?.addMarker(
                    MarkerOptions().position(sydney)
                        .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_baseline_accessibility_24)
                    )
                )
                marker?.tag = "b"
                newMarkers.add(marker)
                newMarkerMap[marker] = data[i]
                map?.setOnMarkerClickListener(this)
            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, id: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, id)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}
