package com.nick.revoluttestproject.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VM : BaseViewModel, View : ViewBinding> : AppCompatActivity() {

    abstract val viewModel: VM
    protected lateinit var binding: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBinding()
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = screenTitle()
        bindLiveData()
    }

    abstract fun screenTitle(): String?

    abstract fun viewBinding(): View

    protected open fun bindLiveData() {}

}