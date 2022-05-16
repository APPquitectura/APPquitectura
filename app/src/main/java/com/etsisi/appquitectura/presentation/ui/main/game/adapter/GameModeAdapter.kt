package com.etsisi.appquitectura.presentation.ui.main.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.etsisi.appquitectura.databinding.ItemGameModeBinding
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode

class GameModeAdapter(
    private val listener: GameListener
): BaseAdapter<ItemGameMode, GameModeAdapter.GameModeViewHolder>() {

    private var mRecyclerView: RecyclerView? = null

    fun interface OnGameModeSelected {
        fun onClicked(item: ItemGameMode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModeViewHolder {
        val view = ItemGameModeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameModeViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    inner class GameModeViewHolder(view: ItemGameModeBinding): BaseHolder<ItemGameMode, ItemGameModeBinding>(view) {
        override fun bind(item: ItemGameMode) {
            view.apply {
                gameMode = item
                this.listener = object : OnGameModeSelected {
                    override fun onClicked(item: ItemGameMode) {
                        this@GameModeAdapter.listener.onGameModeSelected(
                            gameModeIndex = dataSet.indexOf(item),
                            topicsIdSelected = null
                        )
                    }
                }
            }
        }
    }
}