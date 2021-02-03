package com.nick.revoluttestproject.rates.model

data class TotalRates(
    val leadRate: Rate,
    val rates: List<Rate>
)