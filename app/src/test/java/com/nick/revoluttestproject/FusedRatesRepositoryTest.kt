package com.nick.revoluttestproject

import com.nick.revoluttestproject.api.rates.RatesDataSource
import com.nick.revoluttestproject.api.rates.RatesResponse
import com.nick.revoluttestproject.rates.FusedRatesRepository
import com.nick.revoluttestproject.rates.RatesRepository
import com.nick.revoluttestproject.rates.model.RatesFilter
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import kotlin.random.Random

class FusedRatesRepositoryTest {

    private lateinit var repository: RatesRepository
    private val mockedDataSource: RatesDataSource = Mockito.mock(RatesDataSource::class.java)
    private val mockedFilter = RatesFilter("EUR", 1.0)

    private fun createMockedData(filter: RatesFilter): RatesResponse {
        return RatesResponse(
            baseCurrency = filter.currency!!,
            rates = mapOf(
                ("USD" to Random(System.currentTimeMillis()).nextDouble(1.0, 1.5)),
                ("UAH" to Random(System.currentTimeMillis()).nextDouble(30.0, 35.0)),
                ("RUB" to Random(System.currentTimeMillis()).nextDouble(70.0, 80.0))
            )
        )
    }

    @Before
    fun init() {
        repository = FusedRatesRepository(mockedDataSource)
    }

    @Test
    fun `load data from repository with force refresh should return data`() {
        val mockedData = createMockedData(filter = mockedFilter)
        `when`(mockedDataSource.latestRates(mockedFilter)).thenReturn(Single.just(mockedData))
        val request = repository.loadLatestRates(mockedFilter, forceRefresh = true).test()
        request.assertValue { it.leadRate.currency == mockedData.baseCurrency && it.rates.size == mockedData.rates.size }
    }

    @Test
    fun `load data from repository with force refresh with returned error`() {
        val mockedError = Exception("Test error")
        `when`(mockedDataSource.latestRates(mockedFilter)).thenReturn(Single.error(mockedError))
        val request = repository.loadLatestRates(mockedFilter, forceRefresh = true).test()
        request.assertError(mockedError)
    }

    @Test
    fun `load data from repository without force refresh return old data`() {
        val mockedData = createMockedData(filter = mockedFilter)
        `when`(mockedDataSource.latestRates(mockedFilter)).thenReturn(Single.just(mockedData))
        val request = repository.loadLatestRates(mockedFilter, forceRefresh = true).test()
        `when`(mockedDataSource.latestRates(mockedFilter)).thenReturn(Single.just(mockedData))
        val cachedRequest = repository.loadLatestRates(mockedFilter, forceRefresh = false).test()

        request.assertValue { it == cachedRequest.values().first() }
    }

    @Test
    fun `load data from repository with force refresh return new data second time`() {
        `when`(mockedDataSource.latestRates(mockedFilter)).thenReturn(Single.just(createMockedData(filter = mockedFilter)))
        val request = repository.loadLatestRates(mockedFilter, forceRefresh = true).test()
        `when`(mockedDataSource.latestRates(mockedFilter)).thenReturn(Single.just(createMockedData(filter = mockedFilter)))
        val cachedRequest = repository.loadLatestRates(mockedFilter, forceRefresh = true).test()

        request.assertValue { it != cachedRequest.values().first() }
    }

}