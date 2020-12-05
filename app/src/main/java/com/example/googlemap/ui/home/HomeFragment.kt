package com.example.googlemap.ui.home

import android.content.Intent
import android.net.Uri
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.data.model.Symptom
import com.example.googlemap.databinding.FragmentHomeBinding
import com.example.googlemap.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(), RadioGroup.OnCheckedChangeListener {

    private val viewModel by viewModel<HomeViewModel>()
    private val homeAdapter = HomeAdapter(::openDialogSymptomDetail)
    private var canChange = false

    override val layoutResources: Int
        get() = R.layout.fragment_home

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        recyclerViewSymptoms.adapter = homeAdapter
        fetchLocalData()
        observeData()
    }

    override fun initAction() {
        radioGroupChangeLanguage.setOnCheckedChangeListener(this)
        buttonHotline.setOnClickListener { callPhone() }
        buttonMessage.setOnClickListener { sendSMS() }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.radioButtonVietnamese -> if (canChange) updateLanguage(true)
            R.id.radioButtonEnglish -> if (canChange) updateLanguage(false)
        }
    }

    private fun fetchLocalData() {
        val symptoms = mutableListOf(
            Symptom(
                R.drawable.img_headache,
                getString(R.string.title_headche),
                getString(R.string.text_headache_content)
            ),
            Symptom(
                R.drawable.img_cough,
                getString(R.string.title_cough),
                getString(R.string.text_cough_content)
            ),
            Symptom(
                R.drawable.img_fever,
                getString(R.string.title_fever),
                getString(R.string.text_fever_content)
            )
        )
        homeAdapter.submitList(symptoms)
    }

    private fun observeData() {
        viewModel.isVietnameseLanguage.observe(viewLifecycleOwner, Observer {
            if (it) {
                radioButtonVietnamese.isChecked = true
            } else {
                radioButtonEnglish.isChecked = true
            }
            canChange = true
        })
    }

    private fun openDialogSymptomDetail(symptom: Symptom, position: Int) {
        findNavController().navigate(
            HomeFragmentDirections.actionToSymptomDialog(symptom)
        )
    }

    private fun updateLanguage(isVietnamese: Boolean) {
        viewModel.updateLanguage(isVietnamese)
        startActivity(Intent(context, MainActivity::class.java))
        requireActivity().finish()
    }

    private fun callPhone() {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(HOT_LINE)))
    }

    private fun sendSMS() {
        val intentSMS = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(SMS)
            putExtra(EXTRA_SMS_CONTENT, getString(R.string.sms_content))
        }
        startActivity(intentSMS)
    }

    companion object {
        private const val HOT_LINE = "tel: 19009095"
        private const val SMS = "sms: 02462732273"
        private const val EXTRA_SMS_CONTENT = "sms_body"
    }
}
