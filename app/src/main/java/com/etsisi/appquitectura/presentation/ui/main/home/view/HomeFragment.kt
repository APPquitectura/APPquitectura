package com.etsisi.appquitectura.presentation.ui.main.home.view

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentHomeBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.main.adapter.HomeMenuAdapter
import com.etsisi.appquitectura.presentation.ui.main.home.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home,
    HomeViewModel::class
) {

    private val adapter: HomeMenuAdapter?
        get() = mBinding.rvMenu.adapter as? HomeMenuAdapter

    override fun setUpDataBinding(mBinding: FragmentHomeBinding, mViewModel: HomeViewModel) {
        with(mBinding) {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
            rvMenu.adapter = HomeMenuAdapter(
                onMenuItemClickedlistener = { item ->
                    navigator.openSection(item)
                }
            )
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: HomeViewModel) {
        with(mViewModel) {
            sections.observe(viewLifecycleOwner) {
                adapter?.addDataSet(it)
            }
        }
    }

}