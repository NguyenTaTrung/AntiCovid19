package com.example.googlemap.ui.information

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.databinding.FragmentDetailCaseInformationBinding
import com.example.googlemap.utils.showToast
import kotlinx.android.synthetic.main.fragment_detail_case_information.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailCaseInformationFragment : BaseFragment<FragmentDetailCaseInformationBinding>() {

    private val viewModel by viewModel<DetailCaseInformationViewModel>()
    private val navArg by navArgs<DetailCaseInformationFragmentArgs>()
    private val adapter = PlaceAdapter { _, _ -> }

    override val layoutResources: Int
        get() = R.layout.fragment_detail_case_information

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        viewModel.getDetailCase(navArg.bundleIdCase)
        recyclerViewPlace.adapter = adapter
        observeData()
    }

    override fun initAction() {
        toolBarDetailCase.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun observeData() {
        viewModel.detailCase.observe(viewLifecycleOwner, Observer {
            binding.caseInformation = it.information
            adapter.submitList(it.detailPlace)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            context?.showToast(it)
        })
    }
}
