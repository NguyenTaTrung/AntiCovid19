package com.example.googlemap.ui.news.world

import androidx.lifecycle.Observer
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.databinding.FragmentWorldNewsBinding
import com.example.googlemap.ui.dialog.LoadingDialog
import com.example.googlemap.ui.news.NewsAdapter
import com.example.googlemap.ui.web.WebNewsActivity
import com.example.googlemap.utils.showToast
import kotlinx.android.synthetic.main.fragment_hightlight_news.*
import kotlinx.android.synthetic.main.fragment_world_news.*
import kotlinx.android.synthetic.main.fragment_world_news.recyclerViewNews
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorldNewsFragment : BaseFragment<FragmentWorldNewsBinding>() {

    private val viewModel by viewModel<WorldNewsViewModel>()
    private val adapter = NewsAdapter { news, _ ->
        startActivity(WebNewsActivity.getIntent(context, news.link))
    }
    private var dialogLoading: LoadingDialog? = null

    override val layoutResources: Int
        get() = R.layout.fragment_world_news

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        context?.let { dialogLoading = LoadingDialog(it) }
        recyclerViewNews.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) dialogLoading?.show() else dialogLoading?.dismiss()
        })
        viewModel.news.observe(viewLifecycleOwner, Observer {
            binding.size = it.size
            adapter.submitList(it)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            context?.showToast(it)
        })
    }

    override fun initAction() {}
}
