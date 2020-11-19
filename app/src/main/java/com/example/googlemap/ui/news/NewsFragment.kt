package com.example.googlemap.ui.news

import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.databinding.FragmentNewsBinding

class NewsFragment : BaseFragment<FragmentNewsBinding>() {
  override val layoutResources: Int
    get() = R.layout.fragment_news

  override val statusBarColor: Int
    get() = R.color.colorPrimary

  override fun initData() {
  }

  override fun initAction() {
  }

}