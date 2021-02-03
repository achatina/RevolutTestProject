package com.nick.revoluttestproject.rates.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nick.revoluttestproject.rates.adapter.diffutil.Change
import com.nick.revoluttestproject.rates.adapter.diffutil.RatesDiffUtilCallback
import com.nick.revoluttestproject.rates.adapter.diffutil.createCombinedPayload
import com.nick.revoluttestproject.rates.model.Rate

class RatesAdapter(
    private val onClick: (currency: Rate) -> Unit,
    private val onSumChanged: (sum: Double) -> Unit
) : RecyclerView.Adapter<RateViewHolder>() {

    private var items: MutableList<Rate> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder.newInstance(parent, onSumChanged, onClick)
    }

    override fun onBindViewHolder(
        holder: RateViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val combinedChange = createCombinedPayload(payloads as List<Change>)
                ?: return super.onBindViewHolder(holder, position, payloads)

            holder.updateRate(combinedChange.newData, position == 0)

        }
    }

    fun setItems(newItems: List<Rate>) {
        val result = DiffUtil.calculateDiff(RatesDiffUtilCallback(this.items, newItems))
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(newItems)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(items[position], position == 0)
    }

    override fun getItemCount(): Int = items.size

}
