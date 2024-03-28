package com.hoangtien2k3.themovie.utils

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//RxJava
fun <T : Any> Single<T>.applyScheduler(scheduler: Scheduler): Single<T> = subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())
fun <T : Any> Single<T>.applyIoScheduler() = applyScheduler(Schedulers.io())


