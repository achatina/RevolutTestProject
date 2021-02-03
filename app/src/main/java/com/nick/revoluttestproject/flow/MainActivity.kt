package com.nick.revoluttestproject.flow

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.nick.revoluttestproject.MessageHolder
import com.nick.revoluttestproject.R
import com.nick.revoluttestproject.base.BaseActivity
import com.nick.revoluttestproject.databinding.ActivityMainBinding
import com.nick.revoluttestproject.rates.ScreenState
import com.nick.revoluttestproject.rates.adapter.RatesAdapter
import com.nick.revoluttestproject.rates.adapter.RatesItemDecorator
import com.nick.revoluttestproject.rates.model.TotalRates
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), MessageHolder {

    override val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupList()
    }

    override fun screenTitle(): String = getString(R.string.title_rates)

    override fun viewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private fun setupList() {
        binding.ratesList.layoutManager = LinearLayoutManager(this)
        binding.ratesList.addItemDecoration(RatesItemDecorator(resources.getDimensionPixelSize(R.dimen.rates_list_margin)))
        binding.ratesList.adapter = RatesAdapter(
            onClick = viewModel::updateCurrency,
            onSumChanged = viewModel::updateSum
        ).apply {
            registerDataObserver()
        }

        setListScrollListener()
    }

    private fun RecyclerView.Adapter<*>.registerDataObserver() {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                if (fromPosition == 0 || toPosition == 0) {
                    (binding.ratesList.layoutManager as? LinearLayoutManager)?.scrollToPosition(0)
                    clearFocus()
                }
            }
        })
    }

    private fun setListScrollListener() {
        binding.ratesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING) clearFocus()
            }
        })
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager? =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun bindLiveData() {
        viewModel.screenState.observe(this) { state ->
            when (state) {
                is ScreenState.Loading -> handleLoading(state.isLoading)
                is ScreenState.Error -> showError(state.message)
                is ScreenState.Success -> handleSuccess(state.data)
            }
        }
    }

    private fun showError(message: String) {
        //improve error handling and provide proper error messages on layers above
        showDialog(
            getString(R.string.error_title),
            getString(R.string.error_message_general),
            getString(R.string.error_retry)
        ) {
            viewModel.refresh()
        }
    }

    private fun clearFocus() {
        binding.ratesList.clearFocus()
        hideKeyboard()
    }

    private fun handleSuccess(totalRates: TotalRates) {
        (binding.ratesList.adapter as? RatesAdapter)?.setItems(listOf(totalRates.leadRate) + totalRates.rates)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.ratesProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}