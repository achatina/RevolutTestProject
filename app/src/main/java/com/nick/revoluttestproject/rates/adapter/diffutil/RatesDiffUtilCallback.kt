package com.nick.revoluttestproject.rates.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.nick.revoluttestproject.rates.model.Rate

class RatesDiffUtilCallback(
    private var oldItems: List<Rate>,
    private var newItems: List<Rate>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldItems[oldItemPosition]
        val new = newItems[newItemPosition]
        return old.currency == new.currency
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldItems[oldItemPosition]
        val new = newItems[newItemPosition]
        return old == new
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return Change(oldItem, newItem)
    }
}