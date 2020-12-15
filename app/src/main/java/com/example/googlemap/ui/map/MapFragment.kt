package com.example.googlemap.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.data.model.Place
import com.example.googlemap.databinding.FragmentMapBinding
import com.example.googlemap.ui.dialog.BackgroundPermissionDialog
import com.example.googlemap.ui.dialog.PermissionRequestType
import com.example.googlemap.ui.main.BottomNavigationListener
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
    private var myBinding: FragmentMapBinding? = null
    private var markers = mutableListOf<Marker?>()
    private var newMarkers = mutableListOf<Marker?>()
    private var markerMap = mutableMapOf<Marker?, CaseInformation>()
    private var newMarkerMap = mutableMapOf<Marker?, Place>()
    private val viewModel by viewModel<MapViewModel>()
    private var canBackPress = true
    private var visibleMoreMarker = false
    private var visiblePlace = false
    private var bottomNavigationListener: BottomNavigationListener? = null
    private var previousView: View? = null

    override val layoutResources: Int
        get() = R.layout.fragment_map

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) bottomNavigationListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (previousView == null) {
            myBinding = DataBindingUtil.inflate(inflater, layoutResources, container, false)
            mapFragment = SupportMapFragment.newInstance()
            mapFragment?.let { childFragmentManager.beginTransaction().replace(R.id.map, it).commit() }
//            mapFragment =
//                childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            previousView = myBinding?.root
        } else {
            (previousView?.parent as? ViewGroup)?.removeAllViews()
        }
        return previousView
    }

    override fun initData() {
        client = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (!requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            BackgroundPermissionDialog.newInstance(PermissionRequestType.FINE_LOCATION).show(childFragmentManager, null)
        } else {
            if (canBackPress) getCurrentLocation()
            viewModel.startLocationUpdates()
        }
        observeData()
    }

    override fun initAction() {
        textViewSeeDetail.setOnClickListener {
            myBinding?.caseInformation?.let {
                visibleMoreMarker = true
                findNavController().navigate(MapFragmentDirections.actionToDetailCaseInformationFragment(it.id))
            }
        }

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
                        visiblePlace = false
                        cardInformationDetail.visibility = View.GONE
                        cardInformation.visibility = View.VISIBLE
                        newMarkers.clear()
                        canBackPress = true
                        return
                    }
                    findNavController().popBackStack()
                    bottomNavigationListener?.showBottomNav()
                }
            })
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker?.tag?.equals("case") == true) {
            val information = markerMap[marker]
            if (marker != this.marker) {
                cardInformation.visibility = View.VISIBLE
                myBinding?.caseInformation = information
                markers.forEach {
                    it?.isVisible = false
                }
                visiblePlace = true
                information?.id?.let { viewModel.getListDetailInformation(it) }
            } else {
                cardInformation.visibility = View.GONE
                cardInformationDetail.visibility = View.GONE
            }
        } else {
            if (marker != this.marker) {
                cardInformation.visibility = View.GONE
                cardInformationDetail.visibility = View.VISIBLE
                myBinding?.detailCase = newMarkerMap[marker]
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

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationListener = null
        myBinding = null
    }

    private fun observeData() = with(viewModel) {
        listCaseInformation.observe(viewLifecycleOwner, Observer {
            if (!visibleMoreMarker) addMoreLocation(it)
        })

        listDetailInformation.observe(viewLifecycleOwner, Observer {
            if (visiblePlace) {
                canBackPress = false
                addNewLocation(it)
            }
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
                    val currentPosition = LatLng(location.latitude, location.longitude)
                    marker = map?.addMarker(MarkerOptions().position(currentPosition).title(""))
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16F))
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
                marker?.tag = "case"
                markers.add(marker)
                markerMap[marker] = data[i]
                map?.setOnMarkerClickListener(this)
            }
        }
    }

    private fun addNewLocation(data: List<Place>) {
        for (i in data.indices) {
            mapFragment?.getMapAsync { map ->
                val sydney = LatLng(data[i].lat, data[i].lon)
                val marker = map?.addMarker(
                    MarkerOptions().position(sydney)
                        .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_baseline_accessibility_24)
                    )
                )
                marker?.tag = "place"
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
