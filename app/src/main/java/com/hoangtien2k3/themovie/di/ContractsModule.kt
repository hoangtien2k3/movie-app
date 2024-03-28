package com.hoangtien2k3.themovie.di

import androidx.fragment.app.Fragment
import com.hoangtien2k3.themovie.ui.details.DetailsContracts
import com.hoangtien2k3.themovie.ui.favorites.FavoritesContracts
import com.hoangtien2k3.themovie.ui.home.HomeContracts
import com.hoangtien2k3.themovie.ui.search.SearchContracts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object ContractsModule {

    @Provides
    fun homeView(fragment: Fragment): HomeContracts.View {
        return fragment as HomeContracts.View
    }

    @Provides
    fun detailView(fragment: Fragment): DetailsContracts.View {
        return fragment as DetailsContracts.View
    }

    @Provides
    fun searchView(fragment: Fragment): SearchContracts.View {
        return fragment as SearchContracts.View
    }

    @Provides
    fun favoriteView(fragment: Fragment): FavoritesContracts.View {
        return fragment as FavoritesContracts.View
    }

}