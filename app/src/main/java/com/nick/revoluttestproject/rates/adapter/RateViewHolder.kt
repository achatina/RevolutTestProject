package com.nick.revoluttestproject.rates.adapter

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nick.revoluttestproject.R
import com.nick.revoluttestproject.databinding.ItemRateBinding
import com.nick.revoluttestproject.inflate
import com.nick.revoluttestproject.rates.model.Rate
import java.util.*


class RateViewHolder(
    private val binding: ItemRateBinding,
    private val onClick: (currency: Rate) -> Unit,
    onSumChanged: (sum: Double) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val textWatcher = CurrencyAmountTextWatcher(onSumChanged)

    fun bind(
        rate: Rate,
        isLeadView: Boolean
    ) {
        setRateData(rate, isLeadView)

        Glide.with(context())
            .asBitmap()
            .load(
                Uri.parse("file:///android_asset/${rate.currency.toLowerCase(Locale.US)}.png")
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.globe)
            .circleCrop()
            .into(binding.rateCurrencyIcon)

        binding.rateCurrencyInput.removeTextChangedListener(textWatcher)
        binding.rateCurrencyInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.rateCurrencyInput.addTextChangedListener(textWatcher)
            else binding.rateCurrencyInput.removeTextChangedListener(textWatcher)
        }

    }

    private fun setRateData(rate: Rate, isLeadView: Boolean) {

        binding.rateCurrencyTitle.text = rate.currency
        binding.rateCurrencySubtitle.text = rate.description

        binding.rateCurrencyInput.isEnabled = isLeadView
        if (!binding.rateCurrencyInput.hasFocus()) {
            binding.rateCurrencyInput.setText(
                String.format(itemView.context.getString(R.string.currency_value), rate.price)
            )
        }

        if (!isLeadView) {
            binding.root.setOnClickListener {
                onClick.invoke(rate)
            }
        } else {
            binding.root.setOnClickListener(null)
        }
    }

    private fun context() = binding.root.context

    private fun resources() = binding.root.resources

    fun updateRate(rate: Rate, isLeadView: Boolean) {
        setRateData(rate, isLeadView)
    }

    companion object {

        fun newInstance(
            parent: ViewGroup,
            onSumChanged: (sum: Double) -> Unit,
            onClick: (currency: Rate) -> Unit
        ): RateViewHolder {
            return RateViewHolder(
                ItemRateBinding.bind(inflate(R.layout.item_rate, parent)),
                onClick,
                onSumChanged
            )
        }

    }
}