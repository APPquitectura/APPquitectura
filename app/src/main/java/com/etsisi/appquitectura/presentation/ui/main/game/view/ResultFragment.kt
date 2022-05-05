package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentResultBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.main.game.viewmodel.ResultViewModel
import com.etsisi.appquitectura.presentation.utils.getWindowPixels

class ResultFragment: BaseFragment<FragmentResultBinding, ResultViewModel>(
    R.layout.fragment_result,
    ResultViewModel::class
) {
    val args: ResultFragmentArgs by navArgs()

    override fun setUpDataBinding(mBinding: FragmentResultBinding, mViewModel: ResultViewModel) {
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            mViewModel.getRouletteItems(requireContext()).also { list ->
                wheel.addWheelItems(list)

                if (args.isRepeatingMode) {
                    rouletteContainer.isVisible = false
                    showResults()
                } else {
                    spinBtn.setOnClickListener {
                        val numberToRotate = (1..list.size).random()
                        mViewModel.setUserScore(numberToRotate - 1, args.userResult, args.isRepeatingMode)
                        wheel.rotateWheelTo(numberToRotate)
                        wheel.setLuckyWheelReachTheTarget {
                            showResults()
                        }
                        hideSpinBtn()
                    }
                }

                repeatGameBtn.setOnClickListener {
                    navigator.repeatIncorrectAnswers(args.userResult)
                }
            }
        }
    }

    override fun observeViewModel(mViewModel: ResultViewModel) {
    }

    private fun hideSpinBtn() {
        with(mBinding) {
            val windowsWidth = requireActivity().getWindowPixels().first
            val targetX = windowsWidth - spinBtn.left
            ObjectAnimator.ofFloat(spinBtn, View.TRANSLATION_X, targetX.toFloat()).apply {
                doOnEnd {
                    spinBtn.isVisible = false
                }
                start()
            }
        }
    }

    private fun showResults() {
        with(mBinding) {
            val windowsWidth = requireActivity().getWindowPixels().first
            val targetX = windowsWidth - rouletteContainer.left

            val showResultsAnimation = AnimatorInflater.loadAnimator(context, R.animator.show_from_left).apply {
                doOnStart {
                    repeatGameBtn.isVisible = !args.userResult.getAllIncorrectQuestions().isEmpty()
                    resultsContainer.isVisible = true
                }
                setTarget(resultsContainer)
            }

            if (args.isRepeatingMode == false) {
                val obAnimatorAlpha = ObjectAnimator.ofFloat(rouletteContainer, View.ALPHA, 1F, 0F)
                val obAnimatorTranslation = ObjectAnimator.ofFloat(rouletteContainer, View.TRANSLATION_X, targetX.toFloat())
                AnimatorSet().apply {
                    cancel()
                    play(obAnimatorTranslation)
                        .with(obAnimatorAlpha)
                        .after(showResultsAnimation)
                    doOnEnd {
                        rouletteContainer.isVisible = false
                        congratsAnimation.playAnimation()
                    }
                    start()
                }
            } else {
                showResultsAnimation.start()
            }
        }
    }
}