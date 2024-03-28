package com.hoangtien2k3.themovie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
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