package com.hoangtien2k3.themovie.ui.search

import com.hoangtien2k3.themovie.response.CommonMoviesListResponse
import com.hoangtien2k3.themovie.ui.base.BasePresenter
import com.hoangtien2k3.themovie.ui.base.BaseView

interface SearchContracts {
    interface View : BaseView {
        fun loadSearchMoviesList(data : CommonMoviesListResponse)
    }

    interface Presenter : BasePresenter {
        fun callSearchMoviesList(page: Int,query: String)
    }
}