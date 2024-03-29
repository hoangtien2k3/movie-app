package com.hoangtien2k3.themovie.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoangtien2k3.themovie.adapter.CommonMoviesAdapter
import com.hoangtien2k3.themovie.databinding.FragmentSearchBinding
import com.hoangtien2k3.themovie.response.CommonMoviesListResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchContracts.View {

    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var commonMoviesAdapter: CommonMoviesAdapter

    @Inject
    lateinit var searchPresenter: SearchPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchViews = listOf(
            binding.search1,
            binding.search2,
            binding.search3,
            binding.search4,
            binding.search5,
            binding.search6
        )
        for (searchView in searchViews) {
            searchView.setOnClickListener {
                binding.apply {
                    quickSearch.visibility = View.GONE
                    val text: String = searchView.text.toString()
                    if (text.isNotEmpty()) {
                        searchEdt.setText(text)
                    }
                }
            }
        }

        binding.apply {
            searchEdt.addTextChangedListener {
                val search = it.toString()
                if (search.isNotEmpty()) {
                    quickSearch.visibility = View.GONE
                    searchPresenter.callSearchMoviesList(1, search)
                }
            }
            clearText.setOnClickListener {
                searchEdt.setText("")
            }
        }
    }

    override fun loadSearchMoviesList(data: CommonMoviesListResponse) {
        binding.apply {
            commonMoviesAdapter.bind(data.results)

            moviesRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = commonMoviesAdapter
            }

            commonMoviesAdapter.setonItemClickListener {
                val direction = SearchFragmentDirections.actionToDetailFragment(it.id)
                findNavController().navigate(direction)
            }
        }
    }

    override fun showLoading() {
        binding.apply {
            searchLoading.visibility = View.VISIBLE
            moviesRecycler.visibility = View.GONE
        }
    }

    override fun hideLoading() {
        binding.apply {
            searchLoading.visibility = View.GONE
            moviesRecycler.visibility = View.VISIBLE
        }
    }

    override fun responseError(error: String) {
        // TODO
    }

}