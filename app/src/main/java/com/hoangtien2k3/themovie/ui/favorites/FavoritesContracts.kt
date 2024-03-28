package com.hoangtien2k3.themovie.ui.favorites

import com.hoangtien2k3.themovie.db.MoviesEntity
import com.hoangtien2k3.themovie.ui.base.BasePresenter
import com.hoangtien2k3.themovie.ui.base.BaseView

interface  FavoritesContracts {

    interface View : BaseView {
        fun loadFavoriteMovieList(data: MutableList<MoviesEntity>)
        fun showEmpty()
    }

    interface Presenter : BasePresenter {
        fun callFavoritesList()
    }

}