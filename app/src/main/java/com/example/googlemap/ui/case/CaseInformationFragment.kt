package com.example.googlemap.ui.case

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.databinding.FragmentCaseInformationBinding
import com.example.googlemap.ui.dialog.LoadingDialog
import kotlinx.android.synthetic.main.fragment_case_information.*
import kotlinx.android.synthetic.main.fragment_case_information.pullToRefresh
import org.koin.androidx.viewmodel.ext.android.viewModel

class CaseInformationFragment :
    BaseFragment<FragmentCaseInformationBinding>(),
    Toolbar.OnMenuItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private val adapter = CaseInformationAdapter(::openDetailCase)
    private val viewModel by viewModel<CaseInformationViewModel>()
    private val shareViewModel by activityViewModels<CaseShareViewModel>()
    private var dialogLoading: LoadingDialog? = null

    override val layoutResources: Int
        get() = R.layout.fragment_case_information

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        context?.let { dialogLoading = LoadingDialog(it) }
        recyclerViewCaseInformation.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) dialogLoading?.show() else dialogLoading?.dismiss()
        })

        viewModel.caseInformation.observe(viewLifecycleOwner, Observer {
            binding.size = it.size
            adapter.submitList(it)
            pullToRefresh.isRefreshing = false
        })

        shareViewModel.place.observe(viewLifecycleOwner, Observer {
            viewModel.searchFilter(
                it,
                shareViewModel.getInfected(),
                shareViewModel.getDead(),
                shareViewModel.getRecovered()
            )
        })
    }

    override fun initAction() {
        toolbarCaseInformation.setOnMenuItemClickListener(this)
        toolbarCaseInformation.setNavigationOnClickListener { findNavController().popBackStack() }
        pullToRefresh.setOnRefreshListener(this)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.searchFilter -> {
                findNavController().navigate(CaseInformationFragmentDirections.actionToSearchFilterDialog())
                true
            }
            else -> false
        }

    override fun onRefresh() {
        viewModel.getCaseInformation()
    }

    private fun openDetailCase(caseInformation: CaseInformation, position: Int) {

    }
}
