package com.bale_bootcamp.guardiannews.adapter


import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.bale_bootcamp.guardiannews.NewsFragmentFactory
import com.bale_bootcamp.guardiannews.network.NewsApiService



class NewsPagerAdapter(fragmentActivity: FragmentActivity,
                       private val fragmentFactory: (Int) -> Fragment,
                       private val categories: List<String>
): FragmentStateAdapter(fragmentActivity) {
    private val TAG = "NewsPagerAdapter"

    override fun getItemCount(): Int {
        //Log.d(TAG, "getItemCount: ${NewsApiService.Category.values().size}")
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment: position: $position")
        //return NewsFragmentFactory.newInstance(NewsApiService.Category.values()[position])
        return fragmentFactory(position)
    }

}