package com.example.googlemap.ui.statistic

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.databinding.FragmentStatisticsBinding
import com.example.googlemap.ui.dialog.LoadingDialog
import com.example.googlemap.ui.main.BottomNavigationListener
import com.example.googlemap.utils.TimeConst.INPUT_TIME_FORMAT
import com.example.googlemap.utils.TimeConst.PATTERN_TIME_GROUP_CHART
import com.example.googlemap.utils.alertDialog
import com.example.googlemap.utils.showToast
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class StatisticFragment :
    BaseFragment<FragmentStatisticsBinding>(),
    RadioGroup.OnCheckedChangeListener {
    private val viewModel by viewModel<StatisticViewModel>()
    private val navArgs by navArgs<StatisticFragmentArgs>()
    private var dialogLoading: LoadingDialog? = null
    private var bottomNavigationListener: BottomNavigationListener? = null
    private var isAllowNotification = false

    override val layoutResources: Int
        get() = R.layout.fragment_statistics

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) bottomNavigationListener = context
    }

    override fun initData() {
        context?.let { dialogLoading = LoadingDialog(it) }
        navArgs.bundleCountry?.let {
            guidelineTop.setGuidelinePercent(0.16f)
            textViewSeeDetail.visibility = View.GONE
            textViewChart.visibility = View.GONE
            cardViewChart.visibility = View.GONE
            buttonSelectTime.visibility = View.GONE
            imageViewNotification.visibility = View.GONE
            binding.isVisibleRadioButton = false
            binding.isVietNam = true
            binding.country = it
            radioButtonVietnamese.text = it.country
        } ?: observeData()
    }

    override fun initAction() {
        radioGroupToggleInformation.setOnCheckedChangeListener(this)
        textViewSeeDetail.setOnClickListener {
            if (binding.isVietNam == true) {
                findNavController().navigate(StatisticFragmentDirections.actionToCaseInformationFragment())
            } else {
                findNavController().navigate(StatisticFragmentDirections.actionToDetailCountryFragment())
            }
        }
        textViewExtend.setOnClickListener { checkLocationStatus() }
        buttonSelectTime.setOnClickListener { openDatePickerDialog() }
        imageViewNotification.setOnClickListener { allowDisplayNotification() }
    }

    private fun allowDisplayNotification() {
        if (isAllowNotification) {
            context?.alertDialog(
                getString(R.string.text_notice),
                getString(R.string.text_notice_content)
            ) { _, _ ->
                viewModel.updateNotification(false)
                viewModel.cancelAlarm()
            }?.show()
        } else {
            viewModel.startAlarm()
            viewModel.updateNotification(true)
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (id) {
            R.id.radioButtonVietnamese -> viewModel.getVietNamInformation()
            R.id.radioButtonWorld -> viewModel.getGlobalInformation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 50) {
            val manager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                context?.showToast(getString(R.string.msg_gps_result))
                return
            }
            findNavController().navigate(StatisticFragmentDirections.actionToMapFragment())
            bottomNavigationListener?.hideBottomNav()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationListener = null
    }

    private fun observeData() = with(viewModel) {
        binding.isVisibleRadioButton = true
        isLoading.observe(viewLifecycleOwner, Observer {
            if (it) dialogLoading?.show() else dialogLoading?.dismiss()
        })

        isVietNam.observe(viewLifecycleOwner, Observer {
            binding.isVietNam = it
        })

        vietNamInformation.observe(viewLifecycleOwner, Observer {
            binding.country = it
        })

        globalInformation.observe(viewLifecycleOwner, Observer {
            binding.global = it
        })

        message.observe(viewLifecycleOwner, Observer {
            binding.date = it
        })

        isAllowNotification.observe(viewLifecycleOwner, Observer {
            this@StatisticFragment.isAllowNotification = it
            if (it) {
                binding.on = R.drawable.ic_notifications_white_24dp
            } else {
                binding.on = R.drawable.ic_notifications_off_white_24dp
            }
        })

        countryStatus.observe(viewLifecycleOwner, Observer {
            val confirmed = mutableListOf<Int>()
            val deaths = mutableListOf<Int>()
            val recovered = mutableListOf<Int>()
            val xVal = mutableListOf<String>()
            val inputFormat = SimpleDateFormat(INPUT_TIME_FORMAT, Locale.getDefault())
            val outputFormat = SimpleDateFormat(PATTERN_TIME_GROUP_CHART, Locale.getDefault())
            for (i in it.indices) {
                var index = i
                index++
                if (index < it.size) {
                    xVal.add(outputFormat.format(inputFormat.parse(it[index].date)!!))
                    confirmed.add(it[index].confirmed.minus(it[i].confirmed))
                    deaths.add(it[index].deaths.minus(it[i].deaths))
                    recovered.add(it[index].recovered.minus(it[i].recovered))
                }
            }
            addGroupBarChart(xVal, confirmed, deaths, recovered)
        })

        error.observe(viewLifecycleOwner, Observer {
            context?.showToast(it)
        })
    }

    private fun checkLocationStatus() {
        val manager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            context?.alertDialog(
                "",
                getString(R.string.msg_gps_question)
            ) { _, _ ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 50)
            }?.show()
            return
        }
        findNavController().navigate(StatisticFragmentDirections.actionToMapFragment())
        bottomNavigationListener?.hideBottomNav()
    }

    private fun openDatePickerDialog() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.before(Calendar.getInstance().timeInMillis))
        val datePicker =
            MaterialDatePicker.Builder
                .dateRangePicker()
                .setTitleText(getString(R.string.title_dialog_date))
                .setCalendarConstraints(calendarConstraints.build())
                .build()
        datePicker.show(childFragmentManager, null)
        datePicker.addOnPositiveButtonClickListener {
            viewModel.getCountryAllStatus(it.first, it.second, false)
        }
    }

    private fun getBarDataSet(entry: List<BarEntry>, colorColumn: Int) =
        BarDataSet(entry, "").apply {
            color = resources.getColor(colorColumn, requireActivity().theme)
            valueTextColor = resources.getColor(colorColumn, requireActivity().theme)
            valueTextSize = 10f
            valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "" + value.toInt()
                }
            }
        }

    private fun addGroupBarChart(
        xVal: List<String>,
        infectedData: List<Int>,
        deathData: List<Int>,
        recoveredData: List<Int>
    ) {
        val groupSpace = 0.25f
        val barSpace = 0.05f
        val barWidth = 0.2f
        // (barSpace + barWidth) * column + groupSpace = 1.00 -> interval per "group"

        val entryDeath = mutableListOf<BarEntry>()
        val entryRecovered = mutableListOf<BarEntry>()
        val entryInfected = mutableListOf<BarEntry>()

        for (i in deathData.indices) {
            entryDeath.add(BarEntry(i.toFloat(), deathData[i].toFloat()))
            entryRecovered.add(BarEntry(i.toFloat(), recoveredData[i].toFloat()))
            entryInfected.add(BarEntry(i.toFloat(), infectedData[i].toFloat()))
        }

        val data = BarData(
            getBarDataSet(entryInfected, R.color.colorOrange),
            getBarDataSet(entryDeath, R.color.colorRed),
            getBarDataSet(entryRecovered, R.color.colorGreen),
        )
        data.barWidth = barWidth

        groupBarChart.run {
            this.data = data
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                axisMinimum = 0f
                isGranularityEnabled = true
                setCenterAxisLabels(true)
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(xVal)
            }

            description = null
            setNoDataText(getString(R.string.msg_no_data_available))
            setPinchZoom(false)
            setScaleEnabled(false)
            setDrawBarShadow(false)
            setDrawGridBackground(false)
            setVisibleXRangeMaximum(3f)
            moveViewToX(0f)
            legend.isEnabled = false
            xAxis.axisMaximum = 0 + barData.getGroupWidth(groupSpace, barSpace) * xVal.size

            groupBars(0f, groupSpace, barSpace)
            invalidate()
        }
    }
}
