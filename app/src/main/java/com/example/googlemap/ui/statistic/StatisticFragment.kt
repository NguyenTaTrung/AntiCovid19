package com.example.googlemap.ui.statistic

import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.databinding.FragmentStatisticsBinding
import com.example.googlemap.ui.dialog.LoadingDialog
import com.example.googlemap.utils.showToast
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticFragment :
    BaseFragment<FragmentStatisticsBinding>(),
    RadioGroup.OnCheckedChangeListener {

    private val viewModel by viewModel<StatisticViewModel>()
    private val navArgs by navArgs<StatisticFragmentArgs>()
    private var dialogLoading: LoadingDialog? = null

    override val layoutResources: Int
        get() = R.layout.fragment_statistics

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        context?.let { dialogLoading = LoadingDialog(it) }
        navArgs.bundleCountry?.let {
            binding.isVisibleRadioButton = false
            binding.isVietNam = true
            binding.country = it
            radioButtonVietnamese.text = it.country
        } ?: observeData()
        addGroupBarChart()
    }

    override fun initAction() {
        radioGroupToggleInformation.setOnCheckedChangeListener(this)
        textViewSeeDetail.setOnClickListener {
            findNavController().navigate(StatisticFragmentDirections.actionToDetailCountryFragment())
        }
        textViewExtend.setOnClickListener {
            findNavController().navigate(StatisticFragmentDirections.actionToMapFragment())
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, id: Int) {
        when (id) {
            R.id.radioButtonVietnamese -> viewModel.getVietNamInformation()
            R.id.radioButtonWorld -> viewModel.getGlobalInformation()
        }
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

        error.observe(viewLifecycleOwner, Observer {
            context?.showToast(it)
        })
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

    private fun addGroupBarChart() {
        val groupSpace = 0.25f
        val barSpace = 0.05f
        val barWidth = 0.2f
        // (barSpace + barWidth) * column + groupSpace = 1.00 -> interval per "group"

        val xVal = mutableListOf(
            getString(R.string.text_char_monday),
            getString(R.string.text_char_tuesday),
            getString(R.string.text_char_wednesday),
            getString(R.string.text_char_thursday),
            getString(R.string.text_char_friday),
            getString(R.string.text_char_saturday),
            getString(R.string.text_char_sunday)
        )

        val deathData = intArrayOf(2, 1, 4, 0, 5, 2, 4)
        val recoveredData = intArrayOf(0, 1, 0, 0, 0, 1, 2)
        val infectedData = intArrayOf(10, 2, 12, 17, 21, 8, 5)

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
