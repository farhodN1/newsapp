package com.example.neuigkeiten

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.neuigkeiten.ui.search.SearchFragment
import com.example.neuigkeiten.ui.home.HomeFragment
import com.example.neuigkeiten.ui.favorites.FavoritesFragment
import com.example.neuigkeiten.ui.search.SettingsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        HomeFragment(),
        SearchFragment(),
        FavoritesFragment(),
        SettingsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
