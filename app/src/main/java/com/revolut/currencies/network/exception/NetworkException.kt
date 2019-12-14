package com.revolut.currencies.network.exception

import com.revolut.currencies.base.domain.exception.RevolutApiException
import retrofit2.Response
import java.io.IOException

object NetworkException {
    fun httpError(response: Response<Any>?): RevolutApiException {
        val message: String? = null
        var responseBody = ""
        var statusCode = 0
        val errorCode = 0
        response?.let { statusCode = it.code() }
        response?.let {
            responseBody = it.errorBody()!!.string()
            try {
                // in case of handle http API error
            } catch (exception: Exception) {
            }
        }

        var kind = RevolutApiException.Kind.HTTP
        when (statusCode) {
            500 -> kind = RevolutApiException.Kind.SERVER_DOWN
            408 -> kind = RevolutApiException.Kind.TIME_OUT
            401 -> kind = RevolutApiException.Kind.UNAUTHORIZED
        }

        return RevolutApiException(kind, message?.let { message }
            ?: run { "" })
            .setErrorCode(errorCode)
            .setStatusCode(statusCode)
            .setData(responseBody)
    }

    fun networkError(exception: IOException): RevolutApiException {
        return RevolutApiException(RevolutApiException.Kind.NETWORK, exception)
    }

    fun unexpectedError(exception: Throwable): RevolutApiException {
        return RevolutApiException(RevolutApiException.Kind.UNEXPECTED, exception)
    }
}