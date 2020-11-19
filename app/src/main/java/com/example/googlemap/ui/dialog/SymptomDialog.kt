package com.example.googlemap.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.googlemap.R
import com.example.googlemap.databinding.DialogPreviewBinding
import kotlinx.android.synthetic.main.dialog_preview.*

class SymptomDialog : DialogFragment() {

    private var binding: DialogPreviewBinding? = null
    private val navArgs by navArgs<SymptomDialogArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.background_view_news)
        return inflater.inflate(R.layout.dialog_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogPreviewBinding.bind(view)
        binding?.symptom = navArgs.bundleSymptom
        buttonGotIt.setOnClickListener { this.dismiss() }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
