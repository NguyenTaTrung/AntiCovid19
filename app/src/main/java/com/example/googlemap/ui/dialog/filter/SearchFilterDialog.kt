package com.example.googlemap.ui.dialog.filter

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.googlemap.R
import com.example.googlemap.databinding.DialogSearchFilterBinding
import com.example.googlemap.ui.case.CaseShareViewModel
import com.example.googlemap.utils.showToast
import kotlinx.android.synthetic.main.dialog_search_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFilterDialog : DialogFragment(), AdapterView.OnItemSelectedListener {

    private var binding: DialogSearchFilterBinding? = null
    private val viewModel by viewModel<SearchFilterViewModel>()
    private val shareViewModel by activityViewModels<CaseShareViewModel>()
    private var item = ""
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.background_view_news)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_search_filter, container, false)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setGravity(Gravity.CENTER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        initAction()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        item = parent.getItemAtPosition(position).toString()
        this.position = position
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun observeData() {
        viewModel.spinnerArray.observe(viewLifecycleOwner, Observer {
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPlace.adapter = adapter
            }

            shareViewModel.position.observe(viewLifecycleOwner, Observer { pos ->
                spinnerPlace.setSelection(pos)
            })
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            context?.showToast(it)
        })

        shareViewModel.infected.observe(viewLifecycleOwner, Observer {
            checkBoxTreated.isChecked = it
        })

        shareViewModel.dead.observe(viewLifecycleOwner, Observer {
            checkBoxDead.isChecked = it
        })

        shareViewModel.recovered.observe(viewLifecycleOwner, Observer {
            checkBoxRecovered.isChecked = it
        })
    }

    private fun initAction() {
        spinnerPlace.onItemSelectedListener = this
        materialButtonClose.setOnClickListener { dismiss() }
        materialButtonAccept.setOnClickListener { searchFilter() }
    }

    private fun searchFilter() {
        if (checkBoxTreated.isChecked || checkBoxDead.isChecked || checkBoxRecovered.isChecked) {
            shareViewModel.setInfected(checkBoxTreated.isChecked)
            shareViewModel.setDead(checkBoxDead.isChecked)
            shareViewModel.setRecovered(checkBoxRecovered.isChecked)
            shareViewModel.setPlace(item)
            shareViewModel.setPosition(position)
            dismiss()
            return
        }
        context?.showToast(getString(R.string.msg_no_select_status))
    }
}
