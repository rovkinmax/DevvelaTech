package ru.rovkinmax.devvelatech.util

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ru.rovkinmax.ligataxi.rx.RxSchedulers

fun <T> Observable<T>.async(): Observable<T> = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())

fun <T> Flowable<T>.async(): Flowable<T> = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())

fun <T> Single<T>.async(): Single<T> = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())

fun Completable.async(): Completable = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())