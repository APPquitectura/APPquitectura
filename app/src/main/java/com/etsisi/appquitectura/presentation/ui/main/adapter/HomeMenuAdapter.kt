package com.etsisi.appquitectura.presentation.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.etsisi.appquitectura.databinding.ItemHomeBinding
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.common.HomeItemClicked
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHome

class HomeMenuAdapter(
    private val onMenuItemClickedlistener: HomeItemClicked
): BaseAdapter<ItemHome, HomeMenuAdapter.HomeMenuHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMenuHolder {
        val view = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMenuHolder(view)
    }

    inner class HomeMenuHolder(view: ItemHomeBinding): BaseHolder<ItemHome, ItemHomeBinding>(view) {
        override fun bind(item: ItemHome) {
            view.apply {
                listener = onMenuItemClickedlistener
                this.item = item
            }
        }
    }
}