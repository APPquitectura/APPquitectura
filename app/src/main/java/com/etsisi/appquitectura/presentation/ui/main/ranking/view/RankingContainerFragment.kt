package com.etsisi.appquitectura.presentation.ui.main.ranking.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.etsisi.appquitectura.databinding.FragmentContainerRankingBinding
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.ui.main.ranking.adapter.RankingViewPagerAdapter

class RankingContainerFragment: BaseFragment<FragmentContainerRankingBinding, EmptyViewModel>(
    R.layout.fragment_container_ranking,
    EmptyViewModel::class
) {

    override fun setUpDataBinding(mBinding: FragmentContainerRankingBinding, mViewModel: EmptyViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            pager.apply {
                adapter = RankingViewPagerAdapter(this@RankingContainerFragment)

                TabLayoutMediator(tabLayout, this) { tab, position ->
                    tab.text = (this.adapter as? RankingViewPagerAdapter)?.getTabText(position, requireContext())
                }.attach()
            }
        }
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {}
}