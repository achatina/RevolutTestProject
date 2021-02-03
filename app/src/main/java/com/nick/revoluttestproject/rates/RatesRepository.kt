package com.nick.revoluttestproject.rates

import com.nick.revoluttestproject.rates.model.RatesFilter
import com.nick.revoluttestproject.rates.model.TotalRates
import io.reactivex.rxjava3.core.Single

interface RatesRepository {

    fun loadLatestRates(filter: RatesFilter, forceRefresh: Boolean): Single<TotalRates>

}