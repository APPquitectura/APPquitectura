package com.etsisi.appquitectura.presentation.ui.main.ranking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ItemRankingBinding
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.ui.main.ranking.model.ItemRanking

class RankingAdapter : BaseAdapter<ItemRanking, RankingAdapter.RankingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingHolder {
        val view = ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingHolder(view)
    }

    class RankingHolder(view: ItemRankingBinding): BaseHolder<ItemRanking, ItemRankingBinding>(view) {
        override fun bind(item: ItemRanking) {
            view.apply {
                itemRanking = item
                rankingPoints.text = root.context.getString(R.string.ranking_points_value, item.rankingPoints[RankingType.GENERAL] ?: 0)
            }
        }
    }
}