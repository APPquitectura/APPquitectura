package com.etsisi.appquitectura.presentation.ui.main.ranking.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.presentation.ui.main.ranking.view.RankingFragment

class RankingViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    val fragments = mapOf<RankingType, Fragment>(
        RankingType.GENERAL to RankingFragment(RankingType.GENERAL),
        RankingType.WEEKLY to  RankingFragment(RankingType.WEEKLY)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> fragments.getOrElse(RankingType.GENERAL) { RankingFragment(RankingType.GENERAL) }
            else -> fragments.getOrElse(RankingType.WEEKLY) { RankingFragment(RankingType.WEEKLY) }
        }
    }

    fun getTabText(position: Int, context: Context): String {
        return when(position) {
            0 -> context.getString(R.string.ranking_general_title)
            else -> context.getString(R.string.ranking_weekly_title)
        }
    }

}