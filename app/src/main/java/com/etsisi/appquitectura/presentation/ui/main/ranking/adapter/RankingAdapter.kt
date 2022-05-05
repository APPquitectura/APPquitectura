package com.etsisi.appquitectura.presentation.ui.main.ranking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.etsisi.appquitectura.databinding.ItemRankingBinding
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
                user = item.userBO
                position.text = item.position.toString()
            }
        }
    }
}