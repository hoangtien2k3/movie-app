package com.hoangtien2k3.themovie.ui.home

import com.hoangtien2k3.themovie.response.CommonMoviesListResponse
import com.hoangtien2k3.themovie.response.GenresListResponse
import com.hoangtien2k3.themovie.response.UpcomingMoviesListResponse
import com.hoangtien2k3.themovie.ui.base.BasePresenter
import com.hoangtien2k3.themovie.ui.base.BaseView

interface  HomeContracts {
    interface View : BaseView {
        fun loadUpcomingMoviesList(data : UpcomingMoviesListResponse)
        fun loadGenres(data : GenresListResponse)
        fun loadMoviesGenres(data : CommonMoviesListResponse)
        fun loadPopularMoviesList(data : CommonMoviesListResponse)
    }

    interface Presenter : BasePresenter {
        fun callUpcomingMoviesList(page: Int)
        fun callGenres()
        fun callMoviesGenres(page: Int,with_genres: String)
        fun callPopularMoviesList(page: Int)
    }
}