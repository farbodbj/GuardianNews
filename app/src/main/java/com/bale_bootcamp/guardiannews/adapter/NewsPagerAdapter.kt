package com.bale_bootcamp.guardiannews.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bale_bootcamp.guardiannews.NewsFragment


class NewsPagerAdapter(private val fragmentManager: FragmentManager, private val pages: List<String>): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int = pages.size


    override fun getItem(position: Int): Fragment {
        val category: Bundle = Bundle()
        category.putString("category", pages[position])

        val fragment = NewsFragment()
        fragment.arguments = category
        return fragment
    }
}