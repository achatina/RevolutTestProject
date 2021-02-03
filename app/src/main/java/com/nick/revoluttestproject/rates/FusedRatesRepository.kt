package com.nick.revoluttestproject.rates

import com.nick.revoluttestproject.api.rates.RatesDataSource
import com.nick.revoluttestproject.rates.model.Rate
import com.nick.revoluttestproject.rates.model.RatesFilter
import com.nick.revoluttestproject.rates.model.TotalRates
import io.reactivex.rxjava3.core.Single
import java.util.*

class FusedRatesRepository(
    private val dataSource: RatesDataSource
) : RatesRepository {

    private var cachedRates: TotalRates? = null

    override fun loadLatestRates(filter: RatesFilter, forceRefresh: Boolean): Single<TotalRates> {
        val cachedRates = cachedRates
        return if (forceRefresh || cachedRates == null) {
            loadRemoteRates(filter)
        } else {
            filterCachedRates(cachedRates, filter).flatMap { loadRemoteRates(filter) }
        }
    }

    private fun loadRemoteRates(filter: RatesFilter): Single<TotalRates> {
        return dataSource.latestRates(filter).map { response ->
            TotalRates(
                leadRate = Rate(
                    currency = response.baseCurrency,
                    description = Currency.getInstance(response.baseCurrency).displayName,
                    price = filter.sum,
                    priceFactor = filter.sum
                ),
                rates = response.rates.map {
                    Rate(
                        currency = it.key,
                        description = Currency.getInstance(it.key).displayName,
                        price = it.value * filter.sum,
                        priceFactor = it.value
                    )
                }
            )
        }
    }

    private fun filterCachedRates(
        cachedRates: TotalRates,
        filter: RatesFilter
    ): Single<TotalRates> {
        return Single.just(cachedRates).map { totalRates ->
            val currentCurrency = filter.currency ?: totalRates.leadRate.currency
            val currentSum = filter.sum
            totalRates.copy(
                leadRate = totalRates.leadRate.copy(
                    currency = currentCurrency,
                    description = Currency.getInstance(currentCurrency).displayName,
                    price = currentSum,
                    priceFactor = currentSum
                ),
                rates = totalRates.rates.map {
                    it.copy(price = it.priceFactor * currentSum)
                }
            )
        }
    }

}