package com.etsisi.appquitectura.presentation.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.ui.main.view.QuestionFragment

class QuestionsViewPagerAdapter(
    private val questionsList: List<QuestionBO>,
    fragment: Fragment
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = questionsList.size

    override fun createFragment(position: Int): Fragment = QuestionFragment.newInstance()
}