package com.etsisi.appquitectura.presentation.ui.main.view

import android.content.Intent
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentSettingsBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.view.LoginActivity
import com.etsisi.appquitectura.presentation.ui.main.adapter.SettingsAdapter
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.SettingsViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(
    R.layout.fragment_settings,
    SettingsViewModel::class
) {

    private val adapter: SettingsAdapter?
        get() = mBinding.rvSettings.adapter as? SettingsAdapter

    override fun setUpDataBinding(
        mBinding: FragmentSettingsBinding,
        mViewModel: SettingsViewModel
    ) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
            rvSettings.adapter = SettingsAdapter(
                listener = {
                    mViewModel.handleSettings(it)
                }
            )
        }
    }

    override fun observeViewModel(mViewModel: SettingsViewModel) {
        with(mViewModel) {
            sections.observe(viewLifecycleOwner) {
                adapter?.addDataSet(it)
            }
            onLogOut.observe(viewLifecycleOwner, LiveEventObserver {
                //navigator.navigateFromMainToLogin()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
        }
    }
}