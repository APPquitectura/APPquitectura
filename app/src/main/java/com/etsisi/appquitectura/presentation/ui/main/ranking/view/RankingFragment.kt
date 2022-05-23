package com.etsisi.appquitectura.presentation.ui.main.ranking.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentRankingBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.components.RankingItemDecoration
import com.etsisi.appquitectura.presentation.ui.main.ranking.adapter.RankingAdapter
import com.etsisi.appquitectura.presentation.ui.main.ranking.viewmodel.RankingViewModel

class RankingFragment: BaseFragment<FragmentRankingBinding, RankingViewModel>(
    R.layout.fragment_ranking,
    RankingViewModel::class
) {

    private var rankingAdapter: RankingAdapter? = null
        get() = mBinding.rvRanking.adapter as? RankingAdapter

    override fun setUpDataBinding(mBinding: FragmentRankingBinding, mViewModel: RankingViewModel) {
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            rvRanking.apply {
                adapter = RankingAdapter().also {
                    addItemDecoration(RankingItemDecoration(it))
                }
            }
        }
    }

    override fun observeViewModel(mViewModel: RankingViewModel) {
        mViewModel.apply {
            ranking.observe(viewLifecycleOwner) {
                rankingAdapter?.addDataSet(it)
            }
        }
    }
}