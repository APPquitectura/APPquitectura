package com.etsisi.appquitectura.presentation.ui.main.settings.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentSettingsBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.common.SettingsListener
import com.etsisi.appquitectura.presentation.ui.login.view.LoginActivity
import com.etsisi.appquitectura.presentation.ui.main.settings.adapter.SettingsAdapter
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettings
import com.etsisi.appquitectura.presentation.ui.main.settings.viewmodel.SettingsViewModel
import com.etsisi.appquitectura.presentation.utils.startClearActivity

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(
    R.layout.fragment_settings,
    SettingsViewModel::class
), SettingsListener {

    private val adapter: SettingsAdapter?
        get() = mBinding.rvSettings.adapter as? SettingsAdapter

    override fun setUpDataBinding(
        mBinding: FragmentSettingsBinding,
        mViewModel: SettingsViewModel
    ) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            rvSettings.adapter = SettingsAdapter(this@SettingsFragment)
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: SettingsViewModel) {
        with(mViewModel) {
            setGoogleClient(requireActivity(), getString(R.string.default_web_client_id))
            sections.observe(viewLifecycleOwner) {
                adapter?.addDataSet(it)
            }
            onLogOut.observe(viewLifecycleOwner, LiveEventObserver {
                requireActivity().startClearActivity<LoginActivity>()
            })
        }
    }

    override fun onSettingsItemClicked(item: ItemSettings) {
        mViewModel.handleSettings(item)
    }

    override fun onRepeatModeSwitch(enabled: Boolean) {
        mViewModel.enableDisableRepeatMode(enabled)
    }

    override fun isRepeatModeEnabled() = mViewModel.isRepeatModeEnabled()
}