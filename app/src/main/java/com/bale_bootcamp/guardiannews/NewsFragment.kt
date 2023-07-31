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
import com.bale_bootcamp.guardiannews.onlinedatasources.NewsOnlineDataSource
import com.bale_bootcamp.guardiannews.repository.NewsRepository
import com.bale_bootcamp.guardiannews.viewmodel.NewsFragmentViewModel
import kotlinx.coroutines.launch

class NewsFragment : Fragment() {
    private val TAG = "NewsFragment"
    private lateinit var _binding: FragmentNewsBinding
    val binding get() = _binding

    private val viewModel: NewsFragmentViewModel by activityViewModels {
        val onlineDataSource = NewsOnlineDataSource(NewsApi.retrofitApiService)
        val repository = NewsRepository(onlineDataSource, lifecycleScope)
        NewsFragmentViewModel.NewsFragmentViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: view created")
        _binding = FragmentNewsBinding.bind(view)
        Log.d(TAG, "onViewCreated: binding set")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: binding set")
        setNewsList()
        Log.d(TAG, "onCreateView: news list set")
        return binding.root
    }


    private fun setNewsList() {
        val newsRecyclerViewAdapter = NewsAdapter {
            Log.d(TAG, "onItemClicked: $it")
            TODO("onItemClicked")
        }

        lifecycleScope.launch {
            viewModel.responseModel.observe(viewLifecycleOwner) {
                Log.d(TAG, "setNewsList: $it")
                newsRecyclerViewAdapter.submitList(it?.results)
                Log.d(TAG, "setNewsList: list submitted")
            }
        }
    }

}