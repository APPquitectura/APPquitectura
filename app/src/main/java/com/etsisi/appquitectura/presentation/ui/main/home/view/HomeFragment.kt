package com.etsisi.appquitectura.presentation.ui.main.home.view

import androidx.lifecycle.lifecycleScope
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentHomeBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.components.ZoomOutPageTransformer
import com.etsisi.appquitectura.presentation.ui.main.home.adapter.BackgroundPagerAdapter
import com.etsisi.appquitectura.presentation.ui.main.home.adapter.HomeMenuAdapter
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHomeAction
import com.etsisi.appquitectura.presentation.ui.main.home.viewmodel.HomeViewModel
import com.etsisi.appquitectura.presentation.utils.autoScroll

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
                    mViewModel.handleMenuItemSelected(item, requireContext())
                }
            )
            btnInfo.setOnClickListener {
                navigator.openSection(ItemHomeAction.ABOUT)
            }
            backgroundPager.apply {
                adapter = BackgroundPagerAdapter(this@HomeFragment, listOf(
                    R.drawable.carga_1,
                    R.drawable.carga_2,
                    R.drawable.carga_3,
                    R.drawable.carga_4,
                    R.drawable.carga_5,
                    R.drawable.carga_6,
                    R.drawable.carga_7
                ))
                isUserInputEnabled = false
                setPageTransformer(ZoomOutPageTransformer())
                autoScroll(viewLifecycleOwner.lifecycleScope, BackgroundPagerAdapter.REFRESH_RATE_4_SECONDS)
            }
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: HomeViewModel) {
        with(mViewModel) {
            sections.observe(viewLifecycleOwner) {
                adapter?.addDataSet(it)
            }
            onMenuItemClicked.observe(viewLifecycleOwner, LiveEventObserver {
                navigator.openSection(it)
            })
        }
    }

}