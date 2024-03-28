package com.hoangtien2k3.themovie.ui.favorites

import com.hoangtien2k3.themovie.repository.DatabaseRepository
import com.hoangtien2k3.themovie.ui.base.BasePresenterImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class FavoritesPresenter
@Inject constructor(
    private val repository: DatabaseRepository,
    val view: FavoritesContracts.View,
) : FavoritesContracts.Presenter, BasePresenterImpl() {

    companion object{
        const val TAG="FavoritesPresenter"
    }

    override fun callFavoritesList() {
        disposable = repository
            .getAllFavoriteList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    view.loadFavoriteMovieList(it)
                } else {
                    view.showEmpty()
                }
            }
    }

}