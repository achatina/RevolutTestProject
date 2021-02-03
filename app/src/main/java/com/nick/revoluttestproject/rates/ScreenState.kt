package com.nick.revoluttestproject.rates

import com.nick.revoluttestproject.rates.model.TotalRates

sealed class ScreenState {
    class Loading(val isLoading: Boolean) : ScreenState()
    class Error(val message: String) : ScreenState()
    class Success(val data: TotalRates) : ScreenState()
}
