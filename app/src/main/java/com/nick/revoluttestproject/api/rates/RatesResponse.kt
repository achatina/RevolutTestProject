package com.nick.revoluttestproject.api.rates

data class RatesResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)