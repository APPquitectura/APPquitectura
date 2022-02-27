package com.etsisi.appquitectura.presentation.common

import androidx.navigation.NavController
import com.etsisi.appquitectura.presentation.ui.login.view.formscreen.LoginFormFragmentDirections

class Navigator (private val navController: NavController){

    fun openRegisterFragment() {
        val directions = LoginFormFragmentDirections.actionLoginFormToRegisterForm()
        navController.navigate(directions)
    }
}