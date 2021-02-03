package com.nick.revoluttestproject.di

import com.nick.revoluttestproject.api.GsonProvider
import com.nick.revoluttestproject.api.rates.RatesDataSource
import com.nick.revoluttestproject.api.rates.RemoteRatesDataSource
import com.nick.revoluttestproject.flow.MainViewModel
import com.nick.revoluttestproject.rates.FusedRatesRepository
import com.nick.revoluttestproject.rates.RatesRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(get()) }
    factory<RatesRepository> { FusedRatesRepository(get()) }
    factory<RatesDataSource> { RemoteRatesDataSource(get()) }
    single { GsonProvider.provideGson() }
}