package com.revolut.currencies.base.domain.exception

class RevolutApiException : Exception {
    private var data: String? = null
    private var statusCode: Int = 0
    private var errorCode: Int = 0
    var kind: Kind? = null
        private set

    constructor(kind: Kind) {
        this.kind = kind
    }

    constructor(kind: Kind, message: String) : super(message) {
        this.kind = kind
    }

    constructor(kind: Kind, message: String, cause: Throwable) : super(message, cause) {
        this.kind = kind
    }

    constructor(kind: Kind, cause: Throwable) : super(cause) {
        this.kind = kind
    }

    fun getData(): String? {
        return data
    }

    fun setData(data: String): RevolutApiException {
        this.data = data
        return this
    }

    fun getStatusCode(): Int {
        return statusCode
    }

    fun setStatusCode(statusCode: Int): RevolutApiException {
        this.statusCode = statusCode
        return this
    }

    fun getErrorCode(): Int {
        return errorCode
    }

    fun setErrorCode(errorCode: Int): RevolutApiException {
        this.errorCode = errorCode
        return this
    }


    enum class Kind {
        /**
         * An [java.io.IOException] occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED,

        SERVER_DOWN,

        TIME_OUT,

        UNAUTHORIZED
    }
}
