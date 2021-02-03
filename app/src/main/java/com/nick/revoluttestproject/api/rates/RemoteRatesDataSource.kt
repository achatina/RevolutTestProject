package com.nick.revoluttestproject.api.rates

import com.google.gson.Gson
import com.nick.revoluttestproject.rates.model.RatesFilter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRatesDataSource(
    private val gson: Gson
) : RatesDataSource {

    private val service = createRetrofit().create(RatesService::class.java)

    override fun latestRates(filter: RatesFilter) = service.latestRates(filter.currency)

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BaseUrl)
            .client(createOkHttpClient())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    companion object {
        //possible move to BuildConfig for automation release and stage links
        private const val BaseUrl = "https://hiring.revolut.codes"
    }

}