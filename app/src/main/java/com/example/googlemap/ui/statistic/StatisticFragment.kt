package com.example.googlemap.ui.statistic

import android.content.Intent
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.databinding.FragmentStatisticsBinding
import com.example.googlemap.ui.dialog.LoadingDialog
import com.example.googlemap.ui.map.MapActivity
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

        val groupSpace = 0.25f
        val barSpace = 0.05f
        val barWidth = 0.2f
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

        val data1 = intArrayOf(2, 1, 4, 0, 5, 2, 4)
        val data2 = intArrayOf(0, 1, 0, 0, 0, 1, 2)
        val data3 = intArrayOf(10, 2, 12, 17, 21, 8, 5)

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
        set1.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "" + value.toInt()
            }
        }

        val set2 = BarDataSet(entry2, "Số người hồi phục mới")
        set2.color = resources.getColor(R.color.colorGreen, requireActivity().theme)
        set2.valueTextColor = resources.getColor(R.color.colorGreen, requireActivity().theme)
        set2.valueTextSize = 10f
        set2.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "" + value.toInt()
            }
        }

        val set3 = BarDataSet(entry3, "Số người nhiễm mới")
        set3.color = resources.getColor(R.color.colorOrange, requireActivity().theme)
        set3.valueTextColor = resources.getColor(R.color.colorOrange, requireActivity().theme)
        set3.valueTextSize = 10f
        set3.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "" + value.toInt()
            }
        }

        val data = BarData(set3, set1, set2)
        data.barWidth = barWidth

        groupBarChart.data = data
        groupBarChart.description = null
        groupBarChart.setPinchZoom(false)
        groupBarChart.setScaleEnabled(false)
        groupBarChart.setDrawBarShadow(false)
        groupBarChart.setDrawGridBackground(false)

        groupBarChart.setVisibleXRangeMaximum(3f)
        groupBarChart.moveViewToX(0f)

        groupBarChart.legend.isEnabled = false

        groupBarChart.xAxis.axisMinimum = 0f
        groupBarChart.xAxis.axisMaximum =
            0 + groupBarChart.barData.getGroupWidth(groupSpace, barSpace) * xVal.size

        groupBarChart.groupBars(0f, groupSpace, barSpace)

        groupBarChart.invalidate()
    }

    override fun initAction() {
        radioGroupToggleInformation.setOnCheckedChangeListener(this)
        textViewSeeDetail.setOnClickListener {
            findNavController().navigate(StatisticFragmentDirections.actionToDetailCountryFragment())
        }
        textViewExtend.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    MapActivity::class.java
                )
            )
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
}
