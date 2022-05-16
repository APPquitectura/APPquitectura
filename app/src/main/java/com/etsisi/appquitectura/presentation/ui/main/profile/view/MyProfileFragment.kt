package com.etsisi.appquitectura.presentation.ui.main.profile.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentMyProfileBinding
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.BindingAdapter.setImageUrl
import com.etsisi.appquitectura.presentation.ui.main.profile.viewmodel.MyProfileViewModel

class MyProfileFragment: BaseFragment<FragmentMyProfileBinding, MyProfileViewModel>(
    R.layout.fragment_my_profile,
    MyProfileViewModel::class
) {

    override fun observeViewModel(mViewModel: MyProfileViewModel) {
    }

    override fun setUpDataBinding(
        mBinding: FragmentMyProfileBinding,
        mViewModel: MyProfileViewModel
    ) {
       mBinding.apply {
           lifecycleOwner = viewLifecycleOwner
           viewModel = mViewModel
           imgProfile.setImageUrl(CurrentUser.photoUrl.toString())
       }
    }
}