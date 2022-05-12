package com.etsisi.appquitectura.presentation.ui.main.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.etsisi.appquitectura.databinding.ItemClassicGameModeBinding
import com.etsisi.appquitectura.databinding.ItemGameModeBinding
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameModeAction

class GameModeAdapter(
    private val listener: GameListener
): BaseAdapter<ItemGameMode, GameModeAdapter.GameModeViewHolder>() {

    private var mRecyclerView: RecyclerView? = null

    fun interface OnGameModeSelected {
        fun onClicked(item: ItemGameMode)
    }

    enum class GameModeViewType(val value: Int) {
        WITH_OPTIONS(0),
        NO_OPTIONS(1);

        companion object {
            fun parse(value: Int) = values().find { it.value == value }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModeViewHolder {
        val view = when(GameModeViewType.parse(viewType)) {
            GameModeViewType.NO_OPTIONS -> ItemGameModeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> ItemClassicGameModeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return GameModeViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet[position].action) {
            ItemGameModeAction.WeeklyGame -> GameModeViewType.NO_OPTIONS
            is ItemGameModeAction.TestGame -> GameModeViewType.NO_OPTIONS
            is ItemGameModeAction.ClassicGame -> GameModeViewType.WITH_OPTIONS
        }.value
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    fun showClassicOptions () {
        dataSet.clear()
        notifyDataSetChanged()
    }

    inner class GameModeViewHolder(view: ViewDataBinding): BaseHolder<ItemGameMode, ViewDataBinding>(view) {
        override fun bind(item: ItemGameMode) {
            view.apply {
                when(this) {
                    is ItemGameModeBinding -> {
                        gameMode = item
                        this.listener = object : OnGameModeSelected {
                            override fun onClicked(item: ItemGameMode) {
                                if (item.action is ItemGameModeAction.ClassicGame) {
                                    showClassicOptions()
                                } else {
                                    this@GameModeAdapter.listener.onGameModeSelected(gameModeIndex = dataSet.indexOf(item))
                                }
                            }
                        }
                    }
                    is ItemClassicGameModeBinding -> {
                        classicGameModeItem = item.action
                    }
                }
            }
        }
    }
}