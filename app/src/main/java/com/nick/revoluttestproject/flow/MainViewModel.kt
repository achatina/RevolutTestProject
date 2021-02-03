package com.nick.revoluttestproject.flow

import androidx.lifecycle.MutableLiveData
import com.nick.revoluttestproject.base.BaseViewModel
import com.nick.revoluttestproject.rates.RatesRepository
import com.nick.revoluttestproject.rates.ScreenState
import com.nick.revoluttestproject.rates.model.Rate
import com.nick.revoluttestproject.rates.model.RatesFilter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val ratesRepository: RatesRepository
) : BaseViewModel() {

    val screenState = MutableLiveData<ScreenState>()

    private var filter = RatesFilter()
    private var disposable: Disposable? = null

    init {
        loadRates()
    }

    fun updateCurrency(rate: Rate) {
        filter = filter.copy(currency = rate.currency, sum = rate.price)
        loadRates()
    }

    fun updateSum(sum: Double) {
        filter = filter.copy(sum = sum)
        loadRates(forceRefresh = false)
    }

    fun refresh() {
        loadRates()
    }

    //as observing on the main thread it is ok to use setValue for LiveData, anyway, I know it's not thread-safe
    private fun loadRates(forceRefresh: Boolean = true) {
        disposable?.removeFromDisposableContainer()
        disposable = ratesRepository.loadLatestRates(filter, forceRefresh)
            .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { screenState.value = ScreenState.Loading(true) }
            .doOnNext { screenState.value = ScreenState.Loading(false) }
            .subscribe(
                {
                    filter = filter.copy(currency = it.leadRate.currency)
                    screenState.value = ScreenState.Success(it)
                },
                {
                    screenState.value = ScreenState.Error(
                        it.localizedMessage ?: it.message ?: it.toString()
                    )
                }
            )
        disposable?.addToDisposableContainer()
    }

}