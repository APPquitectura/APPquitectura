package com.etsisi.appquitectura.presentation.common

import androidx.navigation.NavController
import com.etsisi.appquitectura.domain.enums.NavType
import com.etsisi.appquitectura.presentation.ui.login.view.formscreen.LoginFormFragmentDirections

class Navigator (private val navController: NavController){

    fun openRegisterFragment() {
        val directions = LoginFormFragmentDirections.navigateToForm(navType = NavType.REGISTER)
        navController.navigate(directions)
    }

    fun openVerifyEmailFragment(emailVerified: Boolean = false) {
        val directions = LoginFormFragmentDirections.navigateToEmailVerificationFragment(emailVerified)
        navController.navigate(directions)
    }

    fun navigateFromLoginToMain() {
        val directions = LoginFormFragmentDirections.navigateLoginToMain()
        navController.navigate(directions)
    }
}