package com.example.googlemap.ui.news

import androidx.fragment.app.Fragment
import com.example.googlemap.R
import com.example.googlemap.base.BaseFragment
import com.example.googlemap.databinding.FragmentNewsBinding
import com.example.googlemap.ui.news.domestic.DomesticNewsFragment
import com.example.googlemap.ui.news.hightlight.HighlightNewsFragment
import com.example.googlemap.ui.news.newest.NewestNewsFragment
import com.example.googlemap.ui.news.world.WorldNewsFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    private var pagerAdapter: NewsPagerAdapter? = null

    override val layoutResources: Int
        get() = R.layout.fragment_news

    override val statusBarColor: Int
        get() = R.color.colorPrimary

    override fun initData() {
        pagerAdapter = NewsPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        val fragments = mutableListOf<Fragment>(
            NewestNewsFragment(),
            HighlightNewsFragment(),
            DomesticNewsFragment(),
            WorldNewsFragment(),
        )
        pagerAdapter?.updateFragments(fragments)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.title_newest_news)
                1 -> tab.text = getString(R.string.title_highlight_news)
                2 -> tab.text = getString(R.string.title_domestic_news)
                3 -> tab.text = getString(R.string.title_world_news)
            }
        }.attach()
    }

    override fun initAction() {
    }
}