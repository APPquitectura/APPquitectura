package com.etsisi.appquitectura.di

import androidx.navigation.NavController
import androidx.room.Room
import com.etsisi.appquitectura.data.datasource.local.AppDatabase
import com.etsisi.appquitectura.data.datasource.local.QuestionsLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.QuestionsRemoteDataSource
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.data.repository.imp.QuestionsRepositoryImp
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.Navigator
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.HomeViewModel
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.MainViewModel
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.PlayViewModel
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.SettingsViewModel
import com.etsisi.appquitectura.utils.Constants.DATABASE_NAME
import com.etsisi.appquitectura.utils.NavigationTracker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(androidApplication(), get(), get(), get(),  get(), get()) }
    viewModel { MainViewModel(androidApplication(), get()) }
    viewModel { EmptyViewModel() }
    viewModel { HomeViewModel() }
    viewModel { SettingsViewModel(get()) }
    viewModel { PlayViewModel(get()) }
}

val presentationModule = module {
    factory { (navController: NavController) -> Navigator(navController) }
    factory { NavigationTracker() }
}

val useCaseModule = module {
    single { Firebase.auth }
    factory { RegisterUseCase(get()) }
    factory { FirebaseLoginUseCase(get()) }
    factory { SendEmailVerificationUseCase() }
    factory { CheckVerificationCodeUseCase(get()) }
    factory { FirebaseLoginWithCredentialsUseCase(get()) }
    factory { LogOutUseCase() }
}

val repositoryModule = module {
    factory<QuestionsRepository> { QuestionsRepositoryImp(get(), get()) }
}

val remoteDataSourceModule = module {
    factory { QuestionsRemoteDataSource() }
}

val localDataSourceModule = module {
    //DAO's
    single { get<AppDatabase>().questionsDao() }

    //LocalDataSource
    factory { QuestionsLocalDataSource(get()) }
}

val databaseModule = module {
    //single { Room.databaseBuilder(androidApplication(), AppDatabase::class.java, DATABASE_NAME).build() }
    single { AppDatabase.getInstance(androidApplication()) }
}

