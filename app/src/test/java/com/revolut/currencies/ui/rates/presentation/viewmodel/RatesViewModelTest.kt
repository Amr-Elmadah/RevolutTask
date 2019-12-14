package com.revolut.currencies.ui.rates.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.revolut.currencies.network.response.Rate
import com.revolut.currencies.network.response.Response
import com.revolut.currencies.ui.rates.domain.repository.RatesRepository
import com.revolut.currencies.util.fromAssets
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
internal class RatesViewModelTest {

    @Mock
    private lateinit var ratesRepository: RatesRepository

    private lateinit var localCurrencies: ArrayList<Rate>

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        localCurrencies =
            Gson().fromAssets(
                context = context,
                fileName = "currencies/currencies.json"
            )
    }

    @Test
    fun checkCurrenciesNotEmptyAndNoZeroValue() {
        val rates =
            "{\"AUD\":0,\"BGN\":1.9469,\"BRL\":4.7701,\"CAD\":1.5269,\"CHF\":1.1224,\"CNY\":7.9091,\"CZK\":25.599,\"DKK\":7.423,\"GBP\":0.89417,\"HKD\":9.0911,\"HRK\":7.4005,\"HUF\":325.01,\"IDR\":17245.0,\"ILS\":4.1517,\"INR\":83.339,\"ISK\":127.22,\"JPY\":128.96,\"KRW\":1298.9,\"MXN\":22.264,\"MYR\":4.7902,\"NOK\":9.7318,\"NZD\":1.7553,\"PHP\":62.309,\"PLN\":4.2988,\"RON\":4.6175,\"RUB\":79.215,\"SEK\":10.543,\"SGD\":1.5928,\"THB\":37.957,\"TRY\":7.5937,\"USD\":1.1581,\"ZAR\":17.743}"
        Mockito.`when`(ratesRepository.getAllRates("EUR"))
            .thenReturn(
                Single.just(
                    Response(
                        base = "EUR", date = Date(), ratesObject = JsonParser().parse(
                            rates
                        ).asJsonObject
                    )
                )
            )

        val testObserver = TestObserver<Response>()

        ratesRepository.getAllRates("EUR")
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertValue { response ->
            val currencies = response.getRates("EUR", localCurrencies)
            currencies.let {
                var isAnyValueZero = false
                currencies.forEach {
                    if (it.value == 0.0) {
                        isAnyValueZero = true
                    }
                }
                currencies.isNotEmpty() && !isAnyValueZero
            }
        }
    }
}