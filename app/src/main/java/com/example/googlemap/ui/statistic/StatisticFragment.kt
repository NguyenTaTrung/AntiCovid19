package com.example.googlemap.ui.statistic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.databinding.FragmentStatisticsBinding
import com.example.googlemap.utils.showToast
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener,
    GoogleMap.OnMarkerClickListener {

    private var client: FusedLocationProviderClient? = null
    private var mapFragment: SupportMapFragment? = null
    private var marker: Marker? = null
    private var binding: FragmentStatisticsBinding? = null
    private var markerMap = mutableMapOf<Marker?, CaseInformation>()
    private val viewModel by viewModel<StatisticViewModel>()

    override val layoutResources: Int
        get() = R.layout.fragment_statistics

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticsBinding.bind(view)
    }

    override fun initData() {
        binding?.isVietNam = true
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkPermission()
        observeData()


        val groupSpace = 0.4f
        val barSpace = 0.05f
        val barWidth = 0.15f
        // (barSpace + barWidth) * column + groupSpace = 1.00 -> interval per "group"

        val xVal = mutableListOf("Th2", "Th3", "Th4", "Th5", "Th6", "Th7", "CN")

        groupBarChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            isGranularityEnabled = true
            setCenterAxisLabels(true)
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(xVal)
        }

        val data1 = intArrayOf(2, 1, 4, 0, 5, 2)
        val data2 = intArrayOf(0, 1, 0, 0, 0, 1)
        val data3 = intArrayOf(10, 2, 12, 17, 21, 8)

        val entry1 = mutableListOf<BarEntry>()
        val entry2 = mutableListOf<BarEntry>()
        val entry3 = mutableListOf<BarEntry>()

        for (i in data1.indices) {
            entry1.add(BarEntry(i.toFloat(), data1[i].toFloat()))
            entry2.add(BarEntry(i.toFloat(), data2[i].toFloat()))
            entry3.add(BarEntry(i.toFloat(), data3[i].toFloat()))
        }

        val set1 = BarDataSet(entry1, "Số người tử vong mới")
        set1.color = resources.getColor(R.color.colorRed, requireActivity().theme)
        set1.valueTextColor = resources.getColor(R.color.colorRed, requireActivity().theme)
        set1.valueTextSize = 10f

        val set2 = BarDataSet(entry2, "Số người hồi phục mới")
        set2.color = resources.getColor(R.color.colorGreen, requireActivity().theme)
        set2.valueTextColor = resources.getColor(R.color.colorGreen, requireActivity().theme)
        set2.valueTextSize = 10f

        val set3 = BarDataSet(entry3, "Số người nhiễm mới")
        set3.color = resources.getColor(R.color.colorOrange, requireActivity().theme)
        set3.valueTextColor = resources.getColor(R.color.colorOrange, requireActivity().theme)
        set3.valueTextSize = 10f


        val data = BarData(set3, set1, set2)
        data.barWidth = barWidth

        groupBarChart.data = data
        groupBarChart.description = null
        groupBarChart.setPinchZoom(false)
        groupBarChart.setScaleEnabled(false)
        groupBarChart.setDrawBarShadow(false)
        groupBarChart.setDrawGridBackground(false)

        groupBarChart.setVisibleXRangeMaximum(3f)

        groupBarChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        groupBarChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        groupBarChart.legend.textSize = 8f

        groupBarChart.xAxis.axisMaximum =
            0 + groupBarChart.barData.getGroupWidth(groupSpace, barSpace) * xVal.size

        groupBarChart.groupBars(0f, groupSpace, barSpace)

        groupBarChart.invalidate()
    }

    override fun initAction() {
        radioGroupToggleInformation.setOnCheckedChangeListener(this)
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

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (id) {
            R.id.radioButtonVietnamese -> {
                viewModel.getVietNamInformation()
                binding?.isVietNam = true
            }
            R.id.radioButtonWorld -> {
                viewModel.getGlobalInformation()
                binding?.isVietNam = false
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun observeData() = with(viewModel) {
        listCaseInformation.observe(viewLifecycleOwner, Observer {
            addMoreLocation(it)
        })

        summary.observe(viewLifecycleOwner, Observer {
            binding?.summary = it
        })

        vietNamInformation.observe(viewLifecycleOwner, Observer {
            binding?.isVietNam = true
            binding?.country = it
        })

        globalInformation.observe(viewLifecycleOwner, Observer {
            binding?.global = it
        })

        error.observe(viewLifecycleOwner, Observer {
            context?.showToast(it)
        })
    }

    private fun checkPermission() {
        val context = context ?: return
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
                    marker = map?.addMarker(MarkerOptions().position(sydney).title(""))
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16F))
                }
            }
        }
    }

    private fun addMoreLocation(data: List<CaseInformation>) {
        val context = context ?: return
        for (i in data.indices) {
            mapFragment?.getMapAsync { map ->
                val sydney = LatLng(data[i].lat, data[i].lon)
                val marker = map?.addMarker(
                    MarkerOptions().position(sydney).icon(bitmapDescriptorFromVector(context))
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
