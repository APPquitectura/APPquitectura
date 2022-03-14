package com.etsisi.appquitectura.presentation.ui.main.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentSettingsBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
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

                }
            )
        }
    }

    override fun observeViewModel(mViewModel: SettingsViewModel) {
        with(mViewModel) {
            sections.observe(viewLifecycleOwner) {
                adapter?.addDataSet(it)
            }
        }
    }
}