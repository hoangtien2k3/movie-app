package com.hoangtien2k3.themovie.di

import android.content.Context
import androidx.room.Room
import com.hoangtien2k3.themovie.db.MoviesDatabase
import com.hoangtien2k3.themovie.db.MoviesEntity
import com.hoangtien2k3.themovie.utils.Constants.MOVIES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MoviesDatabase::class.java, MOVIES_DATABASE
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
    @Provides
    @Singleton
    fun provideDao(db : MoviesDatabase) = db.moviesDoa()


    @Provides
    @Singleton
    fun provideEntity() = MoviesEntity()
}