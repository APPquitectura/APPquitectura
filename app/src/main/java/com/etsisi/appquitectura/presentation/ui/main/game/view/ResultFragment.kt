package com.etsisi.appquitectura.presentation.ui.main.game.view

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentResultBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.main.game.viewmodel.ResultViewModel

class ResultFragment: BaseFragment<FragmentResultBinding, ResultViewModel>(
    R.layout.fragment_result,
    ResultViewModel::class
) {
    val args: ResultFragmentArgs by navArgs()

    override fun observeViewModel(mViewModel: ResultViewModel) {
        //TODO("Not yet implemented")
    }

    override fun setUpDataBinding(mBinding: FragmentResultBinding, mViewModel: ResultViewModel) {
        mBinding.apply {
            mViewModel.getRouletteItems(resources).also { list ->
                roulette.addWheelItems(list)
                rotate.setOnClickListener {
                    val numberToRotate = (1..list.size).random()
                    roulette.rotateWheelTo(numberToRotate)
                    roulette.setLuckyWheelReachTheTarget {
                        roulette.isVisible = false
                        rotate.isVisible = false
                        showResults()
                    }
                }
            }
        }
    }

    fun showResults() {
        mBinding.apply {
            resultsContainer.isVisible = true
            correctQuestions.text = args.userResult.getAllCorrectAnswers().size.toString()
            answersAverage.text = args.userResult.averageUserMillisToAnswer.toString()
        }
    }
}