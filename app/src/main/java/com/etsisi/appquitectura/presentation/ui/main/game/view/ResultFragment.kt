package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentResultBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.main.game.viewmodel.ResultViewModel
import com.etsisi.appquitectura.presentation.utils.getWindowPixels

class ResultFragment: BaseFragment<FragmentResultBinding, ResultViewModel>(
    R.layout.fragment_result,
    ResultViewModel::class
) {

    override fun setUpDataBinding(mBinding: FragmentResultBinding, mViewModel: ResultViewModel) {
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            mViewModel.getRouletteItems(requireContext()).also { list ->
                wheel.addWheelItems(list)

                spinBtn.setOnClickListener {
                    val numberToRotate = (1..list.size).random()
                    mViewModel.updateUserScore(numberToRotate - 1)
                    wheel.rotateWheelTo(numberToRotate)
                    wheel.setLuckyWheelReachTheTarget {
                        showResults()
                    }
                    hideSpinBtn()
                }
            }
        }
    }

    override fun observeViewModel(mViewModel: ResultViewModel) {
        with(mViewModel) {
            showRoulette.observe(viewLifecycleOwner) { show ->
                if (!show) {
                    hideSpinBtn()
                    showResults()
                }
            }
        }
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

            val showResultsAnimation = AnimatorInflater.loadAnimator(context, R.animator.anim_alpha).apply {
                doOnStart {
                    resultsContainer.apply {
                        isVisible = true
                    }
                }
                setTarget(resultsContainer)
            }
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
        }
    }
}