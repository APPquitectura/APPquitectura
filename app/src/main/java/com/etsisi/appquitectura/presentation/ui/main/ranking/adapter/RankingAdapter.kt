package com.etsisi.appquitectura.presentation.ui.main.ranking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ItemRankingBinding
import com.etsisi.appquitectura.databinding.ItemRankingHeaderBinding
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.common.StickyHeaderListener
import com.etsisi.appquitectura.presentation.ui.main.ranking.model.ItemRanking

class RankingAdapter : BaseAdapter<ItemRanking, RankingAdapter.RankingHolder>(), StickyHeaderListener{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingHolder {
        val view = when(viewType) {
            RankingViewType.HEADER.value -> ItemRankingHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return RankingHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].viewType.value
    }

    class RankingHolder(view: ViewDataBinding): BaseHolder<ItemRanking, ViewDataBinding>(view) {
        override fun bind(item: ItemRanking) {
            view.apply {
                when(this) {
                    is ItemRankingHeaderBinding -> {

                    }
                    is ItemRankingBinding -> {
                        itemRanking = item
                        rankingPoints.text = root.context.getString(R.string.ranking_points_value, item.rankingPoints[RankingType.GENERAL] ?: 0)
                    }
                }
            }
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var positionAux = itemPosition
        var headerPosition = 0
        do {
            if (isHeader(positionAux)) {
                headerPosition = positionAux
                break
            }
            positionAux--
        } while (positionAux >= 0)

        return headerPosition
    }

    override fun getHeaderLayout(headerPosition: Int): Int = R.layout.item_ranking_header

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        //TODO("Not yet implemented")
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return dataSet[itemPosition].viewType == RankingViewType.HEADER
    }
}