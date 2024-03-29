package com.hoangtien2k3.themovie.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.hoangtien2k3.themovie.R
import com.hoangtien2k3.themovie.adapter.CommonMoviesAdapter
import com.hoangtien2k3.themovie.adapter.GenreMoviesAdapter
import com.hoangtien2k3.themovie.adapter.UpcomingMoviesAdapter
import com.hoangtien2k3.themovie.databinding.FragmentHomeBinding
import com.hoangtien2k3.themovie.response.CommonMoviesListResponse
import com.hoangtien2k3.themovie.response.GenresListResponse
import com.hoangtien2k3.themovie.response.UpcomingMoviesListResponse
import com.hoangtien2k3.themovie.utils.CheckConnection
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeContracts.View {

    lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var upcomingMoviesAdapter: UpcomingMoviesAdapter

    @Inject
    lateinit var genreMoviesAdapter: GenreMoviesAdapter

    @Inject
    lateinit var commonMoviesAdapter: CommonMoviesAdapter

    @Inject
    lateinit var homePresenter: HomePresenter

    private val pagerHelper: PagerSnapHelper by lazy { PagerSnapHelper() }


    private val checkConnection by lazy { CheckConnection(requireActivity().application) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePresenter.callUpcomingMoviesList(1)
        homePresenter.callGenres()
        homePresenter.callPopularMoviesList(1)

        binding.moviesScrollLay.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    showAndCloseUI(true)
                } else {
                    showAndCloseUI(false)
                }
            })
    }

    override fun loadUpcomingMoviesList(data: UpcomingMoviesListResponse) {
        binding.apply {
            upcomingMoviesAdapter.differ.submitList(data.results)
            topMoviesRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = upcomingMoviesAdapter
            }
            pagerHelper.attachToRecyclerView(topMoviesRecycler)
            topMoviesIndicator.attachToRecyclerView(topMoviesRecycler, pagerHelper)
        }
        commonMoviesAdapter.setonItemClickListener {
            val direction = HomeFragmentDirections.actionToDetailFragment(it.id)
            findNavController().navigate(direction)
        }
    }

    override fun loadGenres(data: GenresListResponse) {
        binding.apply {
            genreMoviesAdapter.differ.submitList(data.genres)
            genresRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = genreMoviesAdapter
            }
            genreMoviesAdapter.setonItemClickListener {
                homePresenter.callMoviesGenres(1, it.id.toString())
            }
        }
    }

    override fun loadMoviesGenres(data: CommonMoviesListResponse) {
        binding.apply {

            lifecycleScope.launchWhenCreated {
                commonMoviesAdapter.bind(data.results)
            }

            lastMoviesRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = commonMoviesAdapter
            }
        }
    }


    override fun loadPopularMoviesList(data: CommonMoviesListResponse) {
        binding.apply {

            lifecycleScope.launchWhenCreated {
                commonMoviesAdapter.bind(data.results)
            }

            lastMoviesRecycler.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = commonMoviesAdapter
            }
        }
    }

    private fun showAndCloseUI(boolean: Boolean) {
        if (boolean) {
            binding.apply {
                fab.setImageResource(R.drawable.ic_arrow_upward_white_24dp)
                fab.setOnClickListener {
                    moviesScrollLay.smoothScrollTo(0, 0)
                    lastMoviesRecycler.smoothScrollToPosition(0);
                }
            }
        } else {
            val isAtTop = !binding.moviesScrollLay.canScrollVertically(-1)
            if (isAtTop) {
                binding.apply {
                    fab.setImageResource(R.drawable.ic_support_chat)
                    fab.setOnClickListener {
                        if (!isFABOpen) showFABMenu()
                        else closeFABMenu()
                    }
                }
            }
        }
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

    override fun showLoading() {
       binding.apply {
           moviesLoading.visibility = View.VISIBLE
           lastMoviesLay.visibility = View.GONE
       }
    }

    override fun hideLoading() {
        binding.apply {
            moviesLoading.visibility = View.GONE
            lastMoviesLay.visibility = View.VISIBLE
        }
    }

    override fun responseError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.onStop()
    }

}