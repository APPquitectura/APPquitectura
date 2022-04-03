package com.etsisi.appquitectura.di

import androidx.navigation.NavController
import androidx.room.Room
import com.etsisi.appquitectura.data.datasource.local.AppDatabase
import com.etsisi.appquitectura.data.datasource.local.QuestionsLocalDataSource
import com.etsisi.appquitectura.data.datasource.local.UsersLocalDataSource
import com.etsisi.appquitectura.data.datasource.remote.QuestionsRemoteDataSource
import com.etsisi.appquitectura.data.datasource.remote.UsersRemoteDataSource
import com.etsisi.appquitectura.data.repository.QuestionsRepository
import com.etsisi.appquitectura.data.repository.UsersRepository
import com.etsisi.appquitectura.data.repository.imp.QuestionsRepositoryImp
import com.etsisi.appquitectura.data.repository.imp.UsersRepositoryImp
import com.etsisi.appquitectura.domain.usecase.CheckUserIsRegisteredUseCase
import com.etsisi.appquitectura.domain.usecase.CheckVerificationCodeUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginUseCase
import com.etsisi.appquitectura.domain.usecase.FirebaseLoginWithCredentialsUseCase
import com.etsisi.appquitectura.domain.usecase.LogOutUseCase
import com.etsisi.appquitectura.domain.usecase.RegisterUseCase
import com.etsisi.appquitectura.domain.usecase.ResetPasswordUseCase
import com.etsisi.appquitectura.domain.usecase.SendEmailVerificationUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateQuestionsUseCase
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.Navigator
import com.etsisi.appquitectura.presentation.dialog.viewmodel.InputTextViewModel
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.RegisterViewModel
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
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get(), get()) }
    viewModel { MainViewModel(androidApplication(), get(), get(), get()) }
    viewModel { EmptyViewModel() }
    viewModel { HomeViewModel() }
    viewModel { InputTextViewModel(get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
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
    factory { LogOutUseCase(get()) }
    factory { CheckUserIsRegisteredUseCase(get()) }
    factory { ResetPasswordUseCase(get()) }
    factory { UpdateQuestionsUseCase(get(), get()) }
}

val repositoryModule = module {
    factory<QuestionsRepository> { QuestionsRepositoryImp(get(), get()) }
    factory<UsersRepository> { UsersRepositoryImp(get(), get()) }
}

val remoteDataSourceModule = module {
    factory { QuestionsRemoteDataSource() }
    factory { UsersRemoteDataSource(get()) }
}

val localDataSourceModule = module {
    //DAO's
    single { get<AppDatabase>().questionsDao() }
    single { get<AppDatabase>().usersDao() }

    //LocalDataSource
    factory { QuestionsLocalDataSource(get()) }
    factory { UsersLocalDataSource(get()) }
}

val databaseModule = module {
    //single { Room.databaseBuilder(androidApplication(), AppDatabase::class.java, DATABASE_NAME).build() }
    single { AppDatabase.getInstance(androidApplication()) }
}

