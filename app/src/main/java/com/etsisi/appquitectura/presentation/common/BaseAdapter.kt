package com.etsisi.appquitectura.presentation.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<item: Any, holder: BaseHolder<item,*>>: RecyclerView.Adapter<holder>(){

    private var dataSet = mutableListOf<item>()

    fun addDataSet(items: List<item>) {
        dataSet.addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size
}

abstract class BaseHolder<T, binding: ViewDataBinding>(val view: binding): RecyclerView.ViewHolder(view.root) {
    abstract fun bind(item: T)
}