package com.etsisi.appquitectura.di

import androidx.navigation.NavController
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.Navigator
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(androidApplication(), get(), get(), get()) }
    viewModel { EmptyViewModel() }
}

val presentationModule = module {
    factory { (navController: NavController) -> Navigator(navController) }
}

val useCaseModule = module {
    single { Firebase.auth }
    factory { RegisterUseCase(get()) }
    factory { FirebaseLoginUseCase(get()) }
    factory { SendEmailVerificationUseCase() }
}