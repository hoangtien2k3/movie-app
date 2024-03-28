package com.hoangtien2k3.themovie.ui.details

import com.hoangtien2k3.themovie.db.MoviesEntity
import com.hoangtien2k3.themovie.response.CreditsLisResponse
import com.hoangtien2k3.themovie.response.DetailsMovieResponse
import com.hoangtien2k3.themovie.ui.base.BasePresenter
import com.hoangtien2k3.themovie.ui.base.BaseView

interface DetailsContracts {
    interface View : BaseView {
        fun loadDetailsMovie(data : DetailsMovieResponse)
        fun loadCreditsMovie(data : CreditsLisResponse)

        //db
        fun updateFavorite(isAdded: Boolean)

    }

    interface Presenter : BasePresenter {
        fun callDetailsMovie(id: Int)
        fun callCreditsMovie(id: Int)
        fun saveMovie(entity: MoviesEntity)
        fun deleteMovie(entity: MoviesEntity)
        fun checkFavorite(id: Int)

    }
}