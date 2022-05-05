package com.etsisi.appquitectura.presentation.ui.main.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.etsisi.appquitectura.databinding.ItemSettingsBinding
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.common.SettingsItemClicked
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettings

class SettingsAdapter(
    private val listener: SettingsItemClicked
): BaseAdapter<ItemSettings, SettingsAdapter.SettingsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsHolder {
        val view = ItemSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsHolder(view)
    }

    inner class SettingsHolder(view: ItemSettingsBinding): BaseHolder<ItemSettings, ItemSettingsBinding>(view) {
        override fun bind(item: ItemSettings) {
            view.apply {
                this.item = item
                this.listener = this@SettingsAdapter.listener
            }
        }
    }
}