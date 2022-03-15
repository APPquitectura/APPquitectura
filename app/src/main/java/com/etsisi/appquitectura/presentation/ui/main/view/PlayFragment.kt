package com.etsisi.appquitectura.presentation.ui.main.view

import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentPlayBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.PlayViewModel

class PlayFragment: BaseFragment<FragmentPlayBinding, PlayViewModel>(
    R.layout.fragment_play,
    PlayViewModel::class
) {
    val args: PlayFragmentArgs by navArgs()

    override fun setUpDataBinding(mBinding: FragmentPlayBinding, mViewModel: PlayViewModel) {
        with(mBinding) {
            navType = args.navType
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: PlayViewModel) {
        //TODO("Not yet implemented")
    }

}