package com.hoangtien2k3.themovie.ui.details

import android.util.Log
import com.hoangtien2k3.themovie.db.MoviesEntity
import com.hoangtien2k3.themovie.repository.ApiRepository
import com.hoangtien2k3.themovie.repository.DatabaseRepository
import com.hoangtien2k3.themovie.ui.base.BasePresenterImpl
import com.hoangtien2k3.themovie.utils.applyIoScheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsPresenter
@Inject constructor(
    private val repository: ApiRepository,
    private val dbRepository: DatabaseRepository,
    val view: DetailsContracts.View,
) : DetailsContracts.Presenter, BasePresenterImpl() {
    override fun callDetailsMovie(id: Int) {
        disposable = repository
            .getMovieDetails(id)
            .applyIoScheduler()
            .subscribe { response ->
                when (response.code()) {
                    in 200..202 ->
                        response.body()?.let { itBody ->
                            Log.e("DetailsPresenter", "itBody : $itBody")
                            view.loadDetailsMovie(itBody)
                        }
                    in 300..399 -> {
                        Log.d("DetailsPresenter", " Redirection messages : ${response.code()}")
                    }
                    in 400..499 -> {
                        Log.d("DetailsPresenter", " Client error responses : ${response.code()}")
                    }
                    in 500..599 -> {
                        Log.d("DetailsPresenter", " Server error responses : ${response.code()}")
                    }
                }
            }
    }

    override fun callCreditsMovie(id: Int) {
        disposable = repository
            .getMovieCredits(id)
            .applyIoScheduler()
            .subscribe { response ->
                when (response.code()) {
                    in 200..202 ->
                        response.body()?.let { itBody ->
                            Log.e("DetailsPresenter", "itBody : $itBody")
                            view.loadCreditsMovie(itBody)
                        }
                    in 300..399 -> {
                        Log.d("DetailsPresenter", " Redirection messages : ${response.code()}")
                    }
                    in 400..499 -> {
                        Log.d("DetailsPresenter", " Client error responses : ${response.code()}")
                    }
                    in 500..599 -> {
                        Log.d("DetailsPresenter", " Server error responses : ${response.code()}")
                    }
                }
            }
    }

    override fun saveMovie(entity: MoviesEntity) {
        disposable = dbRepository.insertMovie(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.updateFavorite(true)
            }
    }

    override fun deleteMovie(entity: MoviesEntity) {
        disposable = dbRepository.deleteMovie(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.updateFavorite(false)
            }
    }

    override fun checkFavorite(id: Int) {
        disposable = dbRepository.existMovie(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.updateFavorite(it)
            }
    }


}