package com.bale_bootcamp.guardiannews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bale_bootcamp.guardiannews.adapter.NewsAdapter
import com.bale_bootcamp.guardiannews.databinding.FragmentNewsBinding
import com.bale_bootcamp.guardiannews.network.NewsApiService
import com.bale_bootcamp.guardiannews.viewmodel.NewsFragmentViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val ONE_MINUTE: Long = 60 * 1000L
class NewsFragment : Fragment() {
    private val TAG = "NewsFragment"

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private var isRefreshing = false

    private var lastRefreshed: Long = 0

    private val viewModel: NewsFragmentViewModel by viewModels {
        NewsFragmentViewModel.NewsFragmentViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: view created")
        _binding = FragmentNewsBinding.bind(view)
        Log.d(TAG, "onViewCreated: binding set")
        refreshNewsList()
        Log.d(TAG, "onViewCreated: news list set")
        Log.d(TAG, "onViewCreated: swipe refresh set")
        setSwipeRefresh()
        Log.d(TAG, "onViewCreated: swipe refresh set")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun refreshNewsList() {
        isRefreshing = true
        val newsRecyclerViewAdapter = NewsAdapter {
            Log.d(TAG, "onItemClicked: $it")
            //TODO("onItemClicked")
        }

        val category = arguments?.getString("category") ?: "search"

        binding.newsRecyclerView.adapter = newsRecyclerViewAdapter
        viewModel.getNews(NewsApiService.Category.findByStr(category),
            LocalDate.now().minusMonths(1),
            LocalDate.now(),
            1,
            10)

        Log.d(TAG, "refreshNewsList: ${viewModel.news}")

        viewModel.news.observe(viewLifecycleOwner) {
            Log.d(TAG, "refreshNewsList: $it")
            lifecycleScope.launch {
                newsRecyclerViewAdapter.submitData(it)
            }
            lastRefreshed = System.currentTimeMillis()
            isRefreshing = false
        }
    }

    private fun setSwipeRefresh(){
        val newsRefreshedToast: Toast = Toast.makeText(context, "News refreshed", Toast.LENGTH_SHORT)
        val refreshedJutsNowToast: Toast = Toast.makeText(context, "News not refreshed", Toast.LENGTH_SHORT)
        binding.refresh.setOnRefreshListener {
            //refreshNewsList()
            //binding.newsRecyclerView.adapter.refresh()
            if(isRefreshing) {
                binding.refresh.isRefreshing = false
                newsRefreshedToast.show()
            }
            if(System.currentTimeMillis() - lastRefreshed < ONE_MINUTE) {
                binding.refresh.isRefreshing = false
                refreshedJutsNowToast.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
