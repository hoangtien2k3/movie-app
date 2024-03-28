package com.hoangtien2k3.themovie.repository

import com.hoangtien2k3.themovie.db.MoviesDao
import com.hoangtien2k3.themovie.db.MoviesEntity
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val dao : MoviesDao) {

    fun getAllFavoriteList ()=dao.getAllMovies()
    fun insertMovie(entity: MoviesEntity) = dao.insertMovie(entity)
    fun deleteMovie(entity: MoviesEntity) = dao.deleteMovie(entity)
    fun existMovie(id: Int) = dao.existMovie(id)

}