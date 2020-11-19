package com.example.googlemap.ui.detail

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.data.model.Country
import com.example.googlemap.databinding.FragmentDetailCountriesBinding
import com.example.googlemap.utils.ModelConst.TOTAL_CONFIRMED
import com.example.googlemap.utils.ModelConst.TOTAL_DEATHS
import com.example.googlemap.utils.ModelConst.TOTAL_RECOVERED
import com.example.googlemap.utils.hideDrawable
import com.example.googlemap.utils.setLeftDrawable
import com.example.googlemap.utils.showToast
import kotlinx.android.synthetic.main.fragment_detail_countries.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailCountryFragment :
    BaseFragment<FragmentDetailCountriesBinding>(),
    TextView.OnEditorActionListener,
    View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener
{

    private val viewModel by viewModel<DetailCountryViewModel>()
    private val adapter = DetailCountryAdapter(::openStatisticFragment) {
        recyclerViewCountries.scrollToPosition(0)
    }

    private var stateButton = 0

    override val layoutResources: Int
        get() = R.layout.fragment_detail_countries

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        recyclerViewCountries.adapter = adapter
        observeData()
    }

    override fun initAction() {
        editTextSearch.setOnEditorActionListener(this)
        toolbarDetailScreen.setNavigationOnClickListener { findNavController().popBackStack() }
        buttonSearch.setOnClickListener(this)
        textViewInfected.setOnClickListener(this)
        textViewDeath.setOnClickListener(this)
        textViewRecovered.setOnClickListener(this)
        pullToRefresh.setOnRefreshListener(this)
    }

    override fun onEditorAction(textView: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) searchCountries()
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonSearch -> searchCountries()
            R.id.textViewInfected -> sortCountries(TOTAL_CONFIRMED)
            R.id.textViewDeath -> sortCountries(TOTAL_DEATHS)
            R.id.textViewRecovered -> sortCountries(TOTAL_RECOVERED)
        }
    }

    override fun onRefresh() {
        viewModel.getDataApi()
    }

    private fun observeData() = with(viewModel) {
        countries.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            pullToRefresh.isRefreshing = false
        })

        error.observe(viewLifecycleOwner, Observer {
            it?.let { context?.showToast(it) }
                ?: context?.showToast(getString(R.string.msg_search_fail))
        })
    }

    private fun searchCountries() {
        val value = editTextSearch.text.toString()
        if (value.isEmpty()) {
            context?.showToast(getString(R.string.text_empty_edittext))
            return
        }
        viewModel.searchCountries(value)
        hideKeyBroad()
        editTextSearch.text.clear()
        hintIcon()
    }

    private fun hideKeyBroad() {
        val view = activity?.currentFocus
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun hintIcon() {
        textViewInfected.hideDrawable()
        textViewDeath.hideDrawable()
        textViewRecovered.hideDrawable()
    }

    private fun updateIcon() {
        when (stateButton) {
            STATE_INFECTED_ASC ->
                textViewInfected.setLeftDrawable(R.drawable.ic_baseline_arrow_downward_24)
            STATE_INFECTED_DESC ->
                textViewInfected.setLeftDrawable(R.drawable.ic_baseline_arrow_upward_24)
            STATE_DEATH_ASC ->
                textViewDeath.setLeftDrawable(R.drawable.ic_baseline_arrow_downward_24)
            STATE_DEATH_DESC ->
                textViewDeath.setLeftDrawable(R.drawable.ic_baseline_arrow_upward_24)
            STATE_RECOVERED_ASC ->
                textViewRecovered.setLeftDrawable(R.drawable.ic_baseline_arrow_downward_24)
            STATE_RECOVERED_DESC ->
                textViewRecovered.setLeftDrawable(R.drawable.ic_baseline_arrow_upward_24)
        }
    }

    private fun sortCountries(key: String) {
        hintIcon()
        when (key) {
            TOTAL_CONFIRMED -> {
                stateButton = if (stateButton == STATE_INFECTED_ASC) {
                    viewModel.sortCountries(key, false)
                    STATE_INFECTED_DESC
                } else {
                    viewModel.sortCountries(key, true)
                    STATE_INFECTED_ASC
                }
            }
            TOTAL_DEATHS -> {
                stateButton = if (stateButton == STATE_DEATH_ASC) {
                    viewModel.sortCountries(key, false)
                    STATE_DEATH_DESC
                } else {
                    viewModel.sortCountries(key, true)
                    STATE_DEATH_ASC
                }
            }
            TOTAL_RECOVERED -> {
                stateButton = if (stateButton == STATE_RECOVERED_ASC) {
                    viewModel.sortCountries(key, false)
                    STATE_RECOVERED_DESC
                } else {
                    viewModel.sortCountries(key, true)
                    STATE_RECOVERED_ASC
                }
            }
        }
        updateIcon()
    }

    private fun openStatisticFragment(country: Country, position: Int) {

    }

    companion object {
        private const val STATE_INFECTED_ASC = 1
        private const val STATE_INFECTED_DESC = 2
        private const val STATE_DEATH_ASC = 3
        private const val STATE_DEATH_DESC = 4
        private const val STATE_RECOVERED_ASC = 5
        private const val STATE_RECOVERED_DESC = 6
    }
}
