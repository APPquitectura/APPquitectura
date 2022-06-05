package com.etsisi.appquitectura.presentation.ui.main.ranking.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentRankingBinding
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.main.ranking.adapter.RankingAdapter
import com.etsisi.appquitectura.presentation.ui.main.ranking.adapter.RankingViewType
import com.etsisi.appquitectura.presentation.ui.main.ranking.model.ItemRanking
import com.etsisi.appquitectura.presentation.ui.main.ranking.viewmodel.RankingViewModel

class RankingFragment(private val rankingType: RankingType): BaseFragment<FragmentRankingBinding, RankingViewModel>(
    R.layout.fragment_ranking,
    RankingViewModel::class
) {

    private val rankingAdapter: RankingAdapter?
        get() = mBinding.rvRanking.adapter as? RankingAdapter

    override fun setUpDataBinding(mBinding: FragmentRankingBinding, mViewModel: RankingViewModel) {
        with(mBinding) {
            rvRanking.adapter = RankingAdapter()
            mViewModel.getRanking(rankingType)
        }
    }

    override fun observeViewModel(mViewModel: RankingViewModel) {
        mViewModel.ranking.observe(viewLifecycleOwner) { rankingList ->
            if (rankingList == null) {
                rankingAdapter?.addDataSet(listOf(ItemRanking(
                    name = requireContext().getString(R.string.ranking_empty),
                    rankingPoints = 0,
                    position = 0,
                    viewType = RankingViewType.HEADER,
                    icon = null
                )
                ))
            } else {
                rankingAdapter?.addDataSet(rankingList)
            }
        }
    }
}