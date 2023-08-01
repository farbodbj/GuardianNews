package com.bale_bootcamp.guardiannews.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bale_bootcamp.guardiannews.utility.Utils


class NewsPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val TAG = "NewsPagerAdapter"
    private val fragments: MutableList<Fragment> = mutableListOf()
    fun addFragment(toAdd: Fragment, pariList: List<Pair<String, String>>): NewsPagerAdapter {
        Log.d(TAG, "addFragment: $toAdd, fragment args: ${Utils.buildStringBundle(pariList)}")
        toAdd.arguments = Utils.buildStringBundle(pariList)
        fragments.add(toAdd)
        return this
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${fragments.size}")
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment = fragments[position]

}