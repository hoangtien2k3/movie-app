package com.hoangtien2k3.themovie.ui.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        floatingTab()
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


    var isFABOpen = false
    private fun floatingTab() {
        binding.fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }
        binding.menuZalo.setOnClickListener {
            closeFABMenu()
            val zaloID = "0828007853"
            val url = "http://zalo.me/$zaloID"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
        binding.menuFacebook.setOnClickListener {
            closeFABMenu()
            val facebookID = "100053705482952"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://m.me/$facebookID"))
            startActivity(intent)
        }
        binding.overlay.setOnClickListener {
            closeFABMenu()
        }
    }

    private fun showFABMenu() {
        isFABOpen = true
        binding.fab.hide()
        binding.fab.setImageResource(R.drawable.ic_floating_btn_close)
        binding.fab.show()
        binding.menuZalo.animate().translationY(-resources.getDimension(R.dimen.marginBottom_zalo))
        binding.menuFacebook.animate().translationY(-resources.getDimension(R.dimen.marginBottom_facebook))
        binding.menuZalo.visibility = View.VISIBLE
        binding.menuFacebook.visibility = View.VISIBLE
        binding.overlay.visibility = View.VISIBLE
    }

    private fun closeFABMenu() {
        isFABOpen = false
        binding.menuZalo.animate().translationY(0F)
        binding.menuFacebook.animate().translationY(0F)
        binding.menuZalo.visibility = View.GONE
        binding.menuFacebook.visibility = View.GONE
        binding.overlay.visibility = View.GONE
        binding.fab.hide()
        binding.fab.setImageResource(R.drawable.ic_support_chat)
        binding.fab.show()
    }

}