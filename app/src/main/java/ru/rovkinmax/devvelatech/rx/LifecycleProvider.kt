package ru.rovkinmax.ligataxi.rx

import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import org.reactivestreams.Publisher
import java.util.concurrent.CancellationException


class LifecycleProvider {
    private var disposable: Disposable? = null
    private var observable: BehaviorSubject<Any>? = null

    internal fun unsubscribe(): Unit {
        if (disposable?.isDisposed?.not() ?: false) {
            observable?.onNext(Any())
            disposable?.dispose()
        }
        disposable = null
        observable = null
    }

    fun <T> lifecycle(): LifecycleTransformer<T> {
        if (observable == null || disposable == null) {
            observable = BehaviorSubject.create()
            disposable = observable!!.subscribe {}
        }
        return LifecycleTransformer(observable!!)
    }

    companion object {
        fun get(fm: FragmentManager): LifecycleProvider {
            val fragment = fm.findFragmentByTag(BundleFragment::class.java.name) as BundleFragment? ?: throw NullPointerException("BundleFragment not attached to FragmentManager")
            return fragment.provider
        }
    }
}

class LifecycleTransformer<T>(private val observable: Observable<Any>) : ObservableTransformer<T, T>, SingleTransformer<T, T>, FlowableTransformer<T, T>, CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return Completable.ambArray(upstream, observable.flatMapCompletable { Completable.error(CancellationException()) })
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> = upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST))

    override fun apply(upstream: Single<T>): SingleSource<T> = upstream.takeUntil(observable.firstOrError())

    override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream.takeUntil(observable)
}

open class BundleFragment : Fragment() {
    internal val provider = LifecycleProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onStop() {
        provider.unsubscribe()
        super.onStop()
    }
}