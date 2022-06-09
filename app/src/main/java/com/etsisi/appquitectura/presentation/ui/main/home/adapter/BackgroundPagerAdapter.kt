package com.etsisi.appquitectura.presentation.ui.main.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etsisi.appquitectura.presentation.ui.main.home.view.BackgroundFragment

class BackgroundPagerAdapter(
    fragment: Fragment,
    private val imagesResId: List<Int>
): FragmentStateAdapter(fragment) {

    companion object {
        const val REFRESH_RATE_4_SECONDS = 7000L
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        return BackgroundFragment.newInstance(imagesResId[position % imagesResId.size])
    }

}