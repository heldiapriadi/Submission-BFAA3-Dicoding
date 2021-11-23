package com.example.githubuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.FollowFragment

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    private val username: String,
    private val tabList: Array<String>
) :
    FragmentStateAdapter(activity) {
    private val numItems = 2

    override fun getItemCount(): Int {
        return numItems
    }

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(username, tabList[position])
    }

}