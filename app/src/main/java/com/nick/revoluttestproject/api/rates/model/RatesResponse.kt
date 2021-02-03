package com.nick.revoluttestproject.api.rates.model

data class RatesResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)