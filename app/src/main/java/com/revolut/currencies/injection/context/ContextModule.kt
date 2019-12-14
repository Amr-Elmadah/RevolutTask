package com.revolut.currencies.injection.context

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val appContext: Context) {
	@Provides
	@Singleton
	fun provideContext() = appContext
}