package com.bale_bootcamp.guardiannews.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bale_bootcamp.guardiannews.utility.Utils


class NewsPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments: MutableList<Fragment> = mutableListOf()
    fun addFragment(toAdd: Fragment, pariList: List<Pair<String, String>>): NewsPagerAdapter {
        toAdd.arguments?.putAll(Utils.buildStringBundle(pariList))
        fragments.add(toAdd)
        return this
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}