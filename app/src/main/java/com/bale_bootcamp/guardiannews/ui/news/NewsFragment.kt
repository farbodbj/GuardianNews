package com.bale_bootcamp.guardiannews.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import com.bale_bootcamp.guardiannews.data.local.model.News
import com.bale_bootcamp.guardiannews.data.network.NewsApiService
import com.bale_bootcamp.guardiannews.databinding.FragmentNewsBinding
import com.bale_bootcamp.guardiannews.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

const val ONE_MINUTE = 60 * 1000

private const val TAG = "NewsFragment"
@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private var isRefreshing = false

    private var lastRefreshed: Long = 0

    private val viewModel: NewsFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: view created")
        if(savedInstanceState == null)
            loadNews()
        setNewsAdapter()
        setSwipeRefresh()
        collectNews()
        Log.d(TAG, "onViewCreated: swipe refresh set")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadNews() {
        val category = arguments?.getString("category") ?: "search"
        viewModel.getNews(NewsApiService.Category.findByStr(category), LocalDate.now())
    }

    private fun setNewsAdapter() {
        val newsRecyclerViewAdapter = NewsAdapter {
            Log.d(TAG, "onItemClicked: $it")
            //TODO("onItemClicked")
        }
        binding.newsRecyclerView.adapter = newsRecyclerViewAdapter
    }


    private fun collectNews() {
        lifecycleScope.launch {
            viewModel.news.collectLatest {
                Log.d(TAG, "refreshNewsList: $it")
                lifecycleScope.launch {
                    (binding.newsRecyclerView.adapter as NewsAdapter).submitData(it)
                }
                lastRefreshed = System.currentTimeMillis()
                isRefreshing = false
            }
        }
    }


    private fun setSwipeRefresh() {
        val newsRefreshedToast: Toast = Toast.makeText(context, "News refreshed", Toast.LENGTH_SHORT)
        val refreshedJutsNowToast: Toast = Toast.makeText(context, "News was refreshed just now", Toast.LENGTH_SHORT)
        binding.refresh.setOnRefreshListener {
            isRefreshing = true
            refreshAPagingAdapter()
            Log.d(TAG, "setSwipeRefresh: refresh")
            if(isRefreshing) {
                binding.refresh.isRefreshing = false
                newsRefreshedToast.show()
            }
            if((System.currentTimeMillis() - lastRefreshed) < ONE_MINUTE) {
                binding.refresh.isRefreshing = false
                refreshedJutsNowToast.show()
            }
        }
    }

    private fun refreshAPagingAdapter() {
        (binding.newsRecyclerView.adapter as NewsAdapter).refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: destroyed")
        _binding = null
    }
}