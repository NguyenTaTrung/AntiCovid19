package com.example.googlemap.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.data.model.Symptom
import com.example.googlemap.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel by viewModel<HomeViewModel>()
    private val homeAdapter = HomeAdapter(::openDialogSymptomDetail)

    override val layoutResources: Int
        get() = R.layout.fragment_home

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
    }

    override fun initData() {
        recyclerViewSymptoms.adapter = homeAdapter
        observeData()
    }

    override fun initAction() {}

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun observeData() {
        viewModel.symptoms.observe(this, Observer {
            homeAdapter.submitList(it)
        })
    }

    private fun openDialogSymptomDetail(symptom: Symptom, position: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionToSymptomDialog(symptom)
        )
    }
}
