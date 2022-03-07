package com.etsisi.appquitectura.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseFragment<binding: ViewDataBinding, viewModel: ViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClass: KClass<viewModel>
): Fragment() {
    protected open val isSharedViewModel: Boolean
        get() = false
    lateinit var mBinding: binding
        private set
    lateinit var mViewModel: viewModel
        private set
    lateinit var navigator: Navigator
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        mViewModel = if (isSharedViewModel) {
            getSharedViewModel(clazz = viewModelClass)
        } else {
            getViewModel(clazz = viewModelClass)
        }

        if (arguments != null) {
            getFragmentArgs(mBinding)
        }

        setUpDataBinding(mBinding, mViewModel)
        observeViewModel(mViewModel)

        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        runCatching {
            findNavController()
        }.getOrNull()?.let { navController ->
            navigator = get { parametersOf(navController) }
        }
    }

    open fun getFragmentArgs(mBinding: binding) {}

    abstract fun observeViewModel(mViewModel: viewModel)

    abstract fun setUpDataBinding(mBinding: binding, mViewModel: viewModel)
}