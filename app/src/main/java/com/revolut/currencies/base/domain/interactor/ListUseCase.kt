package com.revolut.currencies.base.domain.interactor

import io.reactivex.Single

abstract class ListUseCase<in Params, Type> where Type : Any {

    abstract fun build(params: Params): Single<List<Type>>
}