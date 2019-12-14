package com.revolut.currencies.base.presentation.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.revolut.currencies.base.domain.exception.RevolutApiException

class ObservableResource<T> : SingleLiveEvent<T>() {

    val error: SingleLiveEvent<RevolutApiException> = ErrorLiveData()
    val loading: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun observe(
        owner: LifecycleOwner, successObserver: Observer<T>,
        loadingObserver: Observer<Boolean>? = null,
        commonErrorObserver: Observer<RevolutApiException>,
        httpErrorConsumer: Observer<RevolutApiException>? = null,
        networkErrorConsumer: Observer<RevolutApiException>? = null,
        unExpectedErrorConsumer: Observer<RevolutApiException>? = null,
        serverDownErrorConsumer: Observer<RevolutApiException>? = null,
        timeOutErrorConsumer: Observer<RevolutApiException>? = null,
        unAuthorizedErrorConsumer: Observer<RevolutApiException>? = null
    ) {
        super.observe(owner, successObserver)
        loadingObserver?.let { loading.observe(owner, it) }
        (error as ErrorLiveData).observe(
            owner, commonErrorObserver, httpErrorConsumer, networkErrorConsumer, unExpectedErrorConsumer,
            serverDownErrorConsumer, timeOutErrorConsumer, unAuthorizedErrorConsumer
        )
    }


    class ErrorLiveData : SingleLiveEvent<RevolutApiException>() {
        private var ownerRef: LifecycleOwner? = null
        private var httpErrorConsumer: Observer<RevolutApiException>? = null
        private var networkErrorConsumer: Observer<RevolutApiException>? = null
        private var unExpectedErrorConsumer: Observer<RevolutApiException>? = null
        private var commonErrorConsumer: Observer<RevolutApiException>? = null

        private var serverDownErrorConsumer: Observer<RevolutApiException>? = null
        private var timeOutErrorConsumer: Observer<RevolutApiException>? = null
        private var unAuthorizedErrorConsumer: Observer<RevolutApiException>? = null

        override fun setValue(t: RevolutApiException?) {
            ownerRef?.also {
                removeObservers(it)
                t?.let { vale -> addProperObserver(vale) }
                super.setValue(t)
            }

        }

        override fun postValue(value: RevolutApiException) {
            ownerRef?.also {
                removeObservers(it)
                addProperObserver(value)
                super.postValue(value)
            }

        }

        private fun addProperObserver(value: RevolutApiException) {
            when (value.kind) {
                RevolutApiException.Kind.NETWORK -> networkErrorConsumer?.let { observe(ownerRef!!, it) }
                    ?: observe(ownerRef!!, commonErrorConsumer!!)
                RevolutApiException.Kind.HTTP -> httpErrorConsumer?.let { observe(ownerRef!!, it) }
                    ?: observe(ownerRef!!, commonErrorConsumer!!)
                RevolutApiException.Kind.UNEXPECTED -> unExpectedErrorConsumer?.let { observe(ownerRef!!, it) }
                    ?: observe(ownerRef!!, commonErrorConsumer!!)

                RevolutApiException.Kind.SERVER_DOWN -> serverDownErrorConsumer?.let { observe(ownerRef!!, it) }
                    ?: observe(ownerRef!!, commonErrorConsumer!!)

                RevolutApiException.Kind.TIME_OUT -> timeOutErrorConsumer?.let { observe(ownerRef!!, it) }
                    ?: observe(ownerRef!!, commonErrorConsumer!!)

                RevolutApiException.Kind.UNAUTHORIZED -> unAuthorizedErrorConsumer?.let { observe(ownerRef!!, it) }
                    ?: observe(ownerRef!!, commonErrorConsumer!!)

                else -> {
                }
            }
        }


        fun observe(
            owner: LifecycleOwner, commonErrorConsumer: Observer<RevolutApiException>,
            httpErrorConsumer: Observer<RevolutApiException>? = null,
            networkErrorConsumer: Observer<RevolutApiException>? = null,
            unExpectedErrorConsumer: Observer<RevolutApiException>? = null,

            serverDownErrorConsumer: Observer<RevolutApiException>? = null,
            timeOutErrorConsumer: Observer<RevolutApiException>? = null,
            unAuthorizedErrorConsumer: Observer<RevolutApiException>? = null
        ) {
            super.observe(owner, commonErrorConsumer)
            this.ownerRef = owner
            this.commonErrorConsumer = commonErrorConsumer
            this.httpErrorConsumer = httpErrorConsumer
            this.networkErrorConsumer = networkErrorConsumer
            this.unExpectedErrorConsumer = unExpectedErrorConsumer
            this.serverDownErrorConsumer = serverDownErrorConsumer
            this.timeOutErrorConsumer = timeOutErrorConsumer
            this.unAuthorizedErrorConsumer = unAuthorizedErrorConsumer
        }
    }
}