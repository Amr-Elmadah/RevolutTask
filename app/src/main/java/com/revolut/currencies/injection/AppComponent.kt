package com.revolut.currencies.injection

import android.app.Application
import com.revolut.currencies.application.App
import com.revolut.currencies.injection.context.ActivityBuilder
import com.revolut.currencies.injection.modules.AppModule
import com.revolut.currencies.injection.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class,
        NetworkModule::class]
)
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}