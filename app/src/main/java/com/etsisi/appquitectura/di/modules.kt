package com.etsisi.appquitectura.di

import androidx.navigation.NavController
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.Navigator
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { EmptyViewModel() }
}

val presentationModule = module {
    factory { (navController: NavController) -> Navigator(navController) }
}