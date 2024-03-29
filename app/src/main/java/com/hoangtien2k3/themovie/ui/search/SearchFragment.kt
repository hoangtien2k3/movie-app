package com.hoangtien2k3.themovie.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            binding.search6,
            binding.search7,
            binding.search8,
            binding.search9,
            binding.search10,
            binding.search11,
            binding.search12,
            binding.search13,
            binding.search14,
            binding.search15,
            binding.search16,
            binding.search17,
            binding.search18,
            binding.search19,
            binding.search20
        )
        for (searchView in searchViews) {
            searchView.setOnClickListener {
                binding.apply {
                    moviesScrollLay.visibility = View.GONE
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
                    moviesScrollLay.visibility = View.GONE
                    binding.fab.visibility = View.VISIBLE
                    searchPresenter.callSearchMoviesList(1, search)
                }
            }
            clearText.setOnClickListener {
                searchEdt.setText("")
            }
        }

        binding.apply {
            menuFacebook.setOnClickListener { socialNetwork("facebook") }
            menuMessage.setOnClickListener { socialNetwork("message") }
            menuZalo.setOnClickListener { socialNetwork("zalo") }
            menuGithub.setOnClickListener { socialNetwork("github") }
        }


        binding.fab.visibility = View.GONE
        binding.moviesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (firstVisibleItemPosition == 0) {
                    binding.fab.visibility = View.GONE
                } else {
                    binding.fab.visibility = View.VISIBLE
                    binding.fab.setOnClickListener {
                        binding.moviesRecycler.smoothScrollToPosition(0)
                    }
                }
            }
        })

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

    private fun socialNetwork(title: String) {
        val socialNetworkMap = mapOf(
            "zalo" to Pair("0828007853", "http://zalo.me/"),
            "message" to Pair("100053705482952", "http://m.me/"),
            "facebook" to Pair("100077499696008", "http://m.me/"),
            "github" to Pair("hoangtien2k3", "https://github.com/")
        )

        socialNetworkMap[title]?.let { (id, baseUrl) ->
            val url = "$baseUrl$id"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
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