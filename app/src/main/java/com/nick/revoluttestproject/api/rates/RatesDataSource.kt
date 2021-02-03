package com.nick.revoluttestproject.api.rates

import com.nick.revoluttestproject.rates.model.RatesFilter
import io.reactivex.rxjava3.core.Single

interface RatesDataSource {

    fun latestRates(filter: RatesFilter): Single<RatesResponse>

}