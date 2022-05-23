package com.etsisi.appquitectura.presentation.ui.main.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.etsisi.appquitectura.databinding.ItemSettingsBinding
import com.etsisi.appquitectura.databinding.ItemSettingsSwitchBinding
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.common.SettingsListener
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettings
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettingsAction

class SettingsAdapter(
    private val listener: SettingsListener
): BaseAdapter<ItemSettings, SettingsAdapter.SettingsHolder>() {

    enum class SettingsViewType(val value: Int) {
        SWITCH(1),
        NORMAL(2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsHolder {
        val view = when(viewType) {
            SettingsViewType.SWITCH.value -> {
                ItemSettingsSwitchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            else -> {
                ItemSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
        }

        return SettingsHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet[position].action) {
            ItemSettingsAction.ENABLE_REPEATING_MODE -> SettingsViewType.SWITCH.value
            else -> SettingsViewType.NORMAL.value
        }
    }

    inner class SettingsHolder(view: ViewDataBinding): BaseHolder<ItemSettings, ViewDataBinding>(view) {
        override fun bind(item: ItemSettings) {
            view.apply {
                when(this) {
                    is ItemSettingsBinding -> {
                        this.item = item
                        this.listener = this@SettingsAdapter.listener
                    }
                    is ItemSettingsSwitchBinding -> {
                        this.item = item
                        this.rememberSwitch.isChecked = listener.isRepeatModeEnabled()
                        this.rememberSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                            listener.onRepeatModeSwitch(isChecked)
                        }
                    }
                }
            }
        }
    }
}