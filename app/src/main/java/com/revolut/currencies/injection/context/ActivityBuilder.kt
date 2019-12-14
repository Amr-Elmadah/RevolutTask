package com.revolut.currencies.injection.context

import com.revolut.currencies.ui.rates.injection.RatesModule
import com.revolut.currencies.ui.rates.presentation.view.activity.RatesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [(RatesModule::class)])
    abstract fun bindRatesActivity(): RatesActivity
}

