package com.nick.revoluttestproject.rates.model

data class Rate(
    val currency: String,
    val description: String,
    val price: Double,
    val priceFactor: Double
)