package com.revolut.currencies.ui.rates.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolut.currencies.base.domain.exception.RevolutApiException
import com.revolut.currencies.base.presentation.model.ObservableResource
import com.revolut.currencies.base.presentation.viewmodel.BaseViewModel
import com.revolut.currencies.network.response.Rate
import com.revolut.currencies.ui.rates.domain.interactor.GetRatesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase
) : BaseViewModel() {
    var mRates = MutableLiveData<List<Rate>>()
    var baseCurrency = MutableLiveData<Rate>()
    val mRatesObservable = ObservableResource<String>()

    fun loadRates(localCurrencies: ArrayList<Rate>, showLoading: Boolean) {
        baseCurrency.value?.let { currentBaseCurrency ->
            mRatesObservable.loading.value = showLoading
            addDisposable(getRatesUseCase.build(params = currentBaseCurrency.currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                }
                .doAfterTerminate {
                    mRatesObservable.loading.value = false
                }
                .subscribe({
                    mRates.value = it.getRates(
                        baseCurrency = currentBaseCurrency.currency,
                        localCurrencies = localCurrencies
                    ).toMutableList()
                }, { it ->
                    (it as? RevolutApiException).let {
                        mRatesObservable.error.value = it
                    }
                })
            )
        }
    }

    fun changeBaseCurrency(newBaseCurrency: Rate) {
        baseCurrency.value = newBaseCurrency
    }
}