package ru.rovkinmax.devvelatech.rx

import io.reactivex.*
import io.reactivex.functions.Consumer
import org.reactivestreams.Publisher
import ru.rovkinmax.devvelatech.view.EmptyView
import ru.rovkinmax.devvelatech.view.ErrorView
import ru.rovkinmax.devvelatech.view.LoadingView
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object RxDecor {

    fun <T> loading(view: LoadingView) = LoadingViewTransformer<T>(view)

    fun error(view: ErrorView): Consumer<Throwable> {
        return Consumer { e ->
            Timber.tag(javaClass.simpleName).d(e, "")
            when (e) {
                is SocketTimeoutException -> view.showNetworkError()
                is UnknownHostException -> view.showNetworkError()
                else -> view.showErrorMessage(e.message ?: "", false)
            }
        }
    }

    fun <T> emptyStub(view: EmptyView) = EmptyStubTransformer<T>(view)

    class EmptyStubTransformer<T>(private val view: EmptyView) : ObservableTransformer<T, T>, FlowableTransformer<T, T> {

        override fun apply(upstream: Flowable<T>): Publisher<T> {
            return upstream.doOnSubscribe { view.hideEmptyStub() }.switchIfEmpty(emptyFlowable(view))
        }

        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream.doOnSubscribe { view.hideEmptyStub() }.switchIfEmpty(emptyObservable(view))
        }

        private fun <T> emptyObservable(view: EmptyView): Observable<T> {
            return Observable.create<T>({ it.onComplete() }).doOnComplete({ view.showEmptyStub() })
        }

        private fun <T> emptyFlowable(view: EmptyView): Flowable<T> {
            return Flowable.create<T>({ it.onComplete() }, BackpressureStrategy.ERROR).doOnComplete { view.showEmptyStub() }
        }
    }

    class LoadingViewTransformer<T>(private val loadingView: LoadingView) : ObservableTransformer<T, T>, SingleTransformer<T, T>, CompletableTransformer {
        override fun apply(upstream: Completable): CompletableSource {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doOnTerminate { loadingView.hideLoadingIndicator() }
        }

        override fun apply(upstream: Single<T>): SingleSource<T> {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doOnDispose { loadingView.hideLoadingIndicator() }
                    .doOnError { loadingView.hideLoadingIndicator() }
                    .doOnSuccess { loadingView.hideLoadingIndicator() }
        }

        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doOnDispose { loadingView.hideLoadingIndicator() }
                    .doOnTerminate { loadingView.hideLoadingIndicator() }
        }
    }
}