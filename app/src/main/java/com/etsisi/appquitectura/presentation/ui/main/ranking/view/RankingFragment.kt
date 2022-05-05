package com.etsisi.appquitectura.presentation.ui.main.ranking.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentRankingBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.main.ranking.viewmodel.RankingViewModel

class RankingFragment: BaseFragment<FragmentRankingBinding, RankingViewModel>(
    R.layout.fragment_ranking,
    RankingViewModel::class
) {

    override fun setUpDataBinding(mBinding: FragmentRankingBinding, mViewModel: RankingViewModel) {
        TODO("Not yet implemented")
    }

    override fun observeViewModel(mViewModel: RankingViewModel) {
        TODO("Not yet implemented")
    }
}