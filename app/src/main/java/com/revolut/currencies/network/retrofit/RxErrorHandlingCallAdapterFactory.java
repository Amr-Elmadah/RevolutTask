package com.revolut.currencies.network.retrofit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import com.revolut.currencies.base.domain.exception.RevolutApiException;
import com.revolut.currencies.network.exception.NetworkException;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava2CallAdapterFactory mOriginalCallAdapterFactory;

    private RxErrorHandlingCallAdapterFactory() {
        mOriginalCallAdapterFactory = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CallAdapter<?, ?> get(@NotNull final Type returnType, @NotNull final Annotation[] annotations, @NotNull final Retrofit retrofit) {
        return new RxCallAdapterWrapper(mOriginalCallAdapterFactory.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Object> {
        private final CallAdapter<R, Object> mWrappedCallAdapter;

        RxCallAdapterWrapper(final CallAdapter<R, Object> wrapped) {
            mWrappedCallAdapter = wrapped;
        }

        @NotNull
        @Override
        public Type responseType() {
            return mWrappedCallAdapter.responseType();
        }

        @NotNull
        @SuppressWarnings("unchecked")
        @Override
        public Object adapt(@NotNull final Call<R> call) {
            Object result = mWrappedCallAdapter.adapt(call);
            if (result instanceof Single) {
                return ((Single) result).onErrorResumeNext(throwable -> Single.error(asRetrofitException((Throwable) throwable)));
            }
            if (result instanceof Flowable) {
                return ((Flowable) result).onErrorResumeNext(throwable -> {
                    return Flowable.error(asRetrofitException((Throwable) throwable));
                });
            }
            if (result instanceof Observable) {
                return ((Observable) result).onErrorResumeNext(throwable -> {
                    return Observable.error(asRetrofitException((Throwable) throwable));
                });
            }

            if (result instanceof Completable) {
                return ((Completable) result).onErrorResumeNext(throwable -> Completable.error(asRetrofitException(throwable)));
            }

            return result;

        }

        private RevolutApiException asRetrofitException(final Throwable throwable) {
            // We had non-200 and 201 http error
            if (throwable instanceof HttpException) {
                final HttpException httpException = (HttpException) throwable;
                final Response response = httpException.response();

                return NetworkException.INSTANCE.httpError(response);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return NetworkException.INSTANCE.networkError((IOException) throwable);
            }
            // We don't know what happened. We need to simply convert to an unknown error
            return NetworkException.INSTANCE.unexpectedError(throwable);
        }
    }
}