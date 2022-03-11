package com.etsisi.appquitectura.presentation.ui.main.view

import android.os.Bundle
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityMainBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.EmptyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, EmptyViewModel>(
    R.layout.activity_main,
    EmptyViewModel::class
) {

    companion object {
        const val EXTRA_CODE_VERIFIED = "isVerified"
    }

    override fun getActivityArgs(bundle: Bundle) {
        with(bundle) {
            getBoolean(EXTRA_CODE_VERIFIED, true)
        }
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {
        //TODO("Not yet implemented")
    }

    override fun setUpDataBinding(mBinding: ActivityMainBinding, mViewModel: EmptyViewModel) {
        //TODO("Not yet implemented")
    }

    override fun getFragmentContainer(): Int {
        return mBinding.mainContainer.id
    }
}