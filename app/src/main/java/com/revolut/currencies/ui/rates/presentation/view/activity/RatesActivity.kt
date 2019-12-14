package com.revolut.currencies.ui.rates.presentation.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.revolut.currencies.R
import com.revolut.currencies.base.presentation.view.extension.afterTextChanged
import com.revolut.currencies.base.presentation.view.extension.loadFromUrl
import com.revolut.currencies.base.presentation.view.extension.setVisible
import com.revolut.currencies.base.presentation.view.extension.showSnack
import com.revolut.currencies.base.presentation.viewmodel.ViewModelFactory
import com.revolut.currencies.network.response.Rate
import com.revolut.currencies.ui.rates.presentation.view.adapter.RatesAdapter
import com.revolut.currencies.ui.rates.presentation.viewmodel.RatesViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_rates.*
import kotlinx.android.synthetic.main.item_rate.*
import javax.inject.Inject


class RatesActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory<RatesViewModel>

    private val mViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(RatesViewModel::class.java)
    }

    @Inject
    lateinit var manager: LinearLayoutManager

    @Inject
    lateinit var adapter: RatesAdapter

    @Inject
    lateinit var localCurrencies: ArrayList<Rate>

    private var baseCurrencies: ArrayList<Rate> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_rates)
        setupControllers()
    }

    private fun setupControllers() {
        setupRecyclerView()
        observeRatesChange()
        setupCurrencies()
    }

    private fun setupCurrencies() {
        val rate = localCurrencies[0]
        rate.value = 1.0
        mViewModel.changeBaseCurrency(rate)
        val currencies = ArrayList(localCurrencies.map { it.copy() })
        currencies.removeAt(0)
        fillData(currencies)
        loadRates(true)
    }

    private fun loadRates(showLoading: Boolean) {
        mViewModel.loadRates(
            localCurrencies = localCurrencies
            , showLoading = showLoading
        )

        etRate.afterTextChanged {
            onBaseCurrencyEditTextChanged()
        }
    }

    private fun setupRecyclerView() {
        manager.orientation = RecyclerView.VERTICAL
        rvRates.layoutManager = manager
        rvRates.adapter = adapter
        srlRates.setOnRefreshListener {
            loadRates(true)
        }
    }

    @SuppressLint("CheckResult")
    private fun observeRatesChange() {
        mViewModel.baseCurrency.observe(this, Observer { currentBaseCurrency ->
            tvCurrency.text = currentBaseCurrency.currency
            tvCurrencyName.text = currentBaseCurrency.currencyName
            if (currentBaseCurrency.value != 0.0) {
                etRate.setText(currentBaseCurrency.value.toString())
            } else {
                etRate.setText("")
            }
            imgRate.loadFromUrl(
                url = currentBaseCurrency.image,
                isRounded = true,
                placeholder = R.color.colorPrimary
            )
        })

        val h = Handler()
        mViewModel.mRates.observe(this, Observer {
            it?.let { rates ->
                baseCurrencies = ArrayList(rates.map { rate -> rate.copy() })
                onBaseCurrencyEditTextChanged()
                h.postDelayed({ loadRates(false) }, 1000)
            }
        })

        mViewModel.mRatesObservable.observe(this,
            successObserver = Observer {
                it?.let {
                    srlRates.isRefreshing = false
                }
            }, commonErrorObserver = Observer {
                srlRates.isRefreshing = false
            }, loadingObserver = Observer {
                it?.let {
                    srlRates.isRefreshing = it
                }
            }, networkErrorConsumer = Observer {
                srlRates.isRefreshing = false
                llMainContent.showSnack(
                    getString(R.string.internet_connection), Snackbar.LENGTH_INDEFINITE
                )
            })

        adapter.getViewClickedObservable().subscribe { clickedCurrency ->
            baseCurrencies.remove(clickedCurrency)
            mViewModel.baseCurrency.value?.let {
                baseCurrencies.add(0, it)
            }
            mViewModel.changeBaseCurrency(clickedCurrency)
            nsvRates.smoothScrollTo(0, 0)
            loadRates(true)
        }
    }

    private fun onBaseCurrencyEditTextChanged() {
        mViewModel.baseCurrency.value?.let { currency ->
            val text = etRate.text.toString()
            currency.value = if (text.isNotEmpty()) text.toDouble() else 0.0
            val currentCurrencies = baseCurrencies.map { it.copy() }
            for (baseCurrency in currentCurrencies) {
                baseCurrency.value *= currency.value
            }
            fillData(currentCurrencies)
        }
    }

    private fun fillData(rates: List<Rate>) {
        rvRates.setVisible(rates.isNotEmpty())
        llNoData.setVisible(rates.isEmpty())
        runOnUiThread {
            adapter.replaceAllItems(rates.toMutableList())
        }
    }
}
