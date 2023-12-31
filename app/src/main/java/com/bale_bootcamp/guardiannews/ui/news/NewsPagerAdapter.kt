package com.bale_bootcamp.guardiannews.ui.news


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


private const val TAG = "NewsPagerAdapter"
class NewsPagerAdapter(fragmentActivity: FragmentActivity,
                       private val categories: List<String>,
                        private val shouldRefresh: Boolean?
): FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment: category at position: ${categories[position]}")
        val newsFragment = NewsFragment()
        newsFragment.arguments = Bundle().apply {
            putString("category", categories[position])
            putBoolean("shouldUpdate", shouldRefresh ?: false)
        }
        return newsFragment
    }

}