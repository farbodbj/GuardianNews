package com.bale_bootcamp.guardiannews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bale_bootcamp.guardiannews.adapter.NewsAdapter
import com.bale_bootcamp.guardiannews.databinding.FragmentNewsBinding
import com.bale_bootcamp.guardiannews.network.NewsApi
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import com.bale_bootcamp.guardiannews.repository.NewsRepository
import com.bale_bootcamp.guardiannews.viewmodel.NewsFragmentViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class NewsFragment(private val category: String) : Fragment() {
    private val TAG = "NewsFragment"

    private lateinit var _binding: FragmentNewsBinding
    private val binding get() = _binding

    private val viewModel: NewsFragmentViewModel by activityViewModels {
        NewsFragmentViewModel.NewsFragmentViewModelFactory(NewsRepository::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: view created")
        _binding = FragmentNewsBinding.bind(view)
        Log.d(TAG, "onViewCreated: binding set")
        setNewsList()
        Log.d(TAG, "onViewCreated: news list set")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setNewsList() {
        val newsRecyclerViewAdapter = NewsAdapter {
            Log.d(TAG, "onItemClicked: $it")
            TODO("onItemClicked")
        }

        binding.newsRecyclerView.adapter = newsRecyclerViewAdapter
        viewModel.getNews(NewsApiService.Category.findByStr(category), LocalDate.parse("2021-09-01"), LocalDate.parse("2021-09-02"), 1, 10).observe(viewLifecycleOwner) { responseModel ->
            Log.i(TAG, "setNewsList: $responseModel")
            //checking whether responseModel is null or not
            if (responseModel?.results != null) {
                newsRecyclerViewAdapter.submitList(responseModel.results)
                Log.d(TAG, "setNewsList: list submitted")
            } else{
                Log.d(TAG, "setNewsList: responseModel or results is null")
            }
        }
    }
}
