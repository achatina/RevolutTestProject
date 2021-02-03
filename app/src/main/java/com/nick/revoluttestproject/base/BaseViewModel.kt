package com.nick.revoluttestproject.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    protected fun Disposable.addToDisposableContainer() = disposables.add(this)
    protected fun Disposable.removeFromDisposableContainer() = disposables.remove(this)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}