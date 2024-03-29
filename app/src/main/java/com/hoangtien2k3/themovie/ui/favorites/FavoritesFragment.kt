package com.hoangtien2k3.themovie.ui.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoangtien2k3.themovie.R
import com.hoangtien2k3.themovie.adapter.FavoriteMoviesAdapter
import com.hoangtien2k3.themovie.databinding.FragmentFavoritesBinding
import com.hoangtien2k3.themovie.db.MoviesEntity
import com.hoangtien2k3.themovie.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment(), FavoritesContracts.View {

    private lateinit var binding: FragmentFavoritesBinding

    @Inject
    lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter

    @Inject
    lateinit var favoritesPresenter: FavoritesPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesPresenter.callFavoritesList()

        binding.fab.visibility = View.GONE
        binding.favoriteRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (firstVisibleItemPosition == 0) {
                    binding.fab.visibility = View.GONE
                } else {
                    binding.fab.visibility = View.VISIBLE
                    binding.fab.setOnClickListener {
                        binding.favoriteRecycler.smoothScrollToPosition(0)
                    }
                }
            }
        })
    }

    override fun loadFavoriteMovieList(data: MutableList<MoviesEntity>) {

        favoriteMoviesAdapter.bind(data)

        binding.favoriteRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteMoviesAdapter
        }

        favoriteMoviesAdapter.setOnItemClickListener {
            val direction = HomeFragmentDirections.actionToDetailFragment(it.id)
            findNavController().navigate(direction)
        }
    }

    override fun showEmpty() {
        binding.apply {
            emptyItemsLay.visibility = View.VISIBLE
            favoriteRecycler.visibility = View.GONE
        }
    }

    override fun showLoading() {
        binding.apply {
            favLoading.visibility = View.VISIBLE
            favoriteRecycler.visibility = View.GONE
        }
    }

    override fun hideLoading() {
        binding.apply {
            favLoading.visibility = View.GONE
            favoriteRecycler.visibility = View.VISIBLE
        }
    }

    override fun responseError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesPresenter.onStop()
    }

}