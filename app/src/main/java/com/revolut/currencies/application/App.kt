package com.revolut.currencies.application

import android.app.Activity
import com.facebook.stetho.Stetho
import com.revolut.currencies.BuildConfig
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import com.revolut.currencies.injection.DaggerAppComponent
import javax.inject.Inject

class App : BaseApp(), HasActivityInjector {
    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initStetho()
        initLeakCanary()
    }

    private fun initStetho() {
        if (BuildConfig.QA) {
            Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build()
            )
        }
    }

    private fun initDagger() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    private fun initLeakCanary() {
        LeakCanary.install(this)
    }
}