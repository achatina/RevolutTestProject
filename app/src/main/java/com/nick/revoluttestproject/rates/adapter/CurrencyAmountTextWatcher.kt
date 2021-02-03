package com.nick.revoluttestproject.rates.adapter

import android.text.Editable
import android.text.TextWatcher

class CurrencyAmountTextWatcher(
    private val onSumChanged: (sum: Double) -> Unit
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            //replace ',' on '.' for supporting currency in different locales
            onSumChanged.invoke(s.toString().replace(",", ".").toDoubleOrNull() ?: 1.0)
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}