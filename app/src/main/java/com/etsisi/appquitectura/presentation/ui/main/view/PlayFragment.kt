package com.etsisi.appquitectura.presentation.ui.main.view

import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentPlayBinding
import com.etsisi.appquitectura.domain.model.QuestionAge
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.domain.model.QuestionLevel
import com.etsisi.appquitectura.domain.model.QuestionSubject
import com.etsisi.appquitectura.domain.model.QuestionTopic
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.PlayFragmentListener
import com.etsisi.appquitectura.presentation.ui.main.adapter.QuestionsViewPagerAdapter
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.PlayViewModel
import com.google.android.material.tabs.TabLayoutMediator

class PlayFragment: BaseFragment<FragmentPlayBinding, PlayViewModel>(
    R.layout.fragment_play,
    PlayViewModel::class
), PlayFragmentListener {

    val args: PlayFragmentArgs by navArgs()
    private var questionsList = listOf(QuestionBO("XXX", "title1", QuestionSubject.COMPOSICION, QuestionLevel.DIFFICULT,QuestionAge.MIDDLE_AGE,QuestionTopic.ART_NOUVEAU))

    override fun setUpDataBinding(mBinding: FragmentPlayBinding, mViewModel: PlayViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            lifecycle.addObserver(mViewModel)
            viewModel = mViewModel
            navType = args.navType
            listener = this@PlayFragment
            viewPager.adapter = QuestionsViewPagerAdapter(questionsList, this@PlayFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = questionsList[position].title
            }.attach()
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: PlayViewModel) {
        with(mViewModel) {
            questions.observe(viewLifecycleOwner) {
                it
            }
        }
    }

    override fun onGameMode(item: ItemGameMode) {
        navigator.startGame()
    }

}