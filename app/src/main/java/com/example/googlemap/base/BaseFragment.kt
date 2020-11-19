package com.example.googlemap.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    @get:LayoutRes
    protected abstract val layoutResources: Int

    protected abstract val statusBarColor: Int

    private var _binding: T? = null

    protected val binding: T
        get() = _binding ?: throw IllegalStateException(EXCEPTION)

    private var previousView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (previousView == null) {
            previousView = DataBindingUtil
                .inflate<T>(inflater, layoutResources, container, false)
                .apply { _binding = this }.root
        } else {
            (previousView?.parent as? ViewGroup)?.removeAllViews()
        }
        return previousView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor =
            resources.getColor(statusBarColor, requireActivity().theme)
        initData()
        initAction()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    protected abstract fun initData()

    protected abstract fun initAction()

    companion object {
        private const val EXCEPTION = "Binding only is valid after onCreateView"
    }
}
