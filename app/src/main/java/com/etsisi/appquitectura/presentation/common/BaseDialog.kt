package com.etsisi.appquitectura.presentation.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.presentation.utils.checkDialogOpened
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseDialog<binding: ViewDataBinding, viewModel: ViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClass: KClass<viewModel>
): DialogFragment() {

    protected lateinit var mBinding: binding
    private set
    protected lateinit var mViewModel: viewModel
    private set
    protected lateinit var navigator: Navigator
    private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        mViewModel = getViewModel(clazz = viewModelClass)

        if (arguments != null) {
            getFragmentArgs(mBinding)
        }

        setUpDataBinding(mBinding, mViewModel)
        observeViewModel(mViewModel)

        mBinding.root.requestFocus()
        return mBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.dialog_no_actionbar)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        runCatching {
            findNavController()
        }.getOrNull()?.let { navController ->
            navigator = get { parametersOf(navController) }
        }
        (context as? FragmentActivity)?.let {
            it.checkDialogOpened()
        }
    }

    open fun getFragmentArgs(mBinding: binding) {}

    protected abstract fun setUpDataBinding(binding: binding, viewModel: viewModel)

    protected abstract fun observeViewModel(viewModel: viewModel)
}