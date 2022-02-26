package com.etsisi.quizz.di

import androidx.navigation.NavController
import com.etsisi.quizz.presentation.common.EmptyViewModel
import com.etsisi.quizz.presentation.common.Navigator
import com.etsisi.quizz.presentation.ui.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { EmptyViewModel() }
}

val presentationModule = module {
    factory { (navController: NavController) -> Navigator(navController) }
}