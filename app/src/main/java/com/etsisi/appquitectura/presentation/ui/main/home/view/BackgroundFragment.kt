package com.etsisi.appquitectura.presentation.ui.main.home.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentBackgroundBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.BindingAdapter.setImageRes
import com.etsisi.appquitectura.presentation.common.EmptyViewModel

class BackgroundFragment(
    private val imageRes: Int
): BaseFragment<FragmentBackgroundBinding, EmptyViewModel>(
    R.layout.fragment_background,
    EmptyViewModel::class
) {
    companion object {
        fun newInstance(imageRes: Int) = BackgroundFragment(imageRes)
    }
    override fun observeViewModel(mViewModel: EmptyViewModel) {}

    override fun setUpDataBinding(mBinding: FragmentBackgroundBinding, mViewModel: EmptyViewModel) {
        mBinding.image.setImageRes(imageRes)
    }
}