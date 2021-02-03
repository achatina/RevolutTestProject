package com.nick.revoluttestproject.api.rates

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesService {

    @GET("/api/android/latest")
    fun latestRates(@Query("base") currency: String?): Single<RatesResponse>

}