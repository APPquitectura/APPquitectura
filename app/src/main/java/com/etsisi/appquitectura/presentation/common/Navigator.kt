package com.etsisi.appquitectura.presentation.common

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.navigation.NavController
import com.etsisi.appquitectura.LoginDirections
import com.etsisi.appquitectura.MainDirections
import com.etsisi.appquitectura.domain.model.UserGameScoreBO
import com.etsisi.appquitectura.presentation.dialog.enums.DialogType
import com.etsisi.appquitectura.presentation.dialog.model.DialogConfig
import com.etsisi.appquitectura.presentation.ui.login.view.formscreen.LoginFormFragmentDirections
import com.etsisi.appquitectura.presentation.ui.main.MainActivity
import com.etsisi.appquitectura.presentation.ui.main.game.view.PlayFragmentDirections
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHomeAction
import com.etsisi.appquitectura.presentation.ui.main.home.view.HomeFragmentDirections
import com.etsisi.appquitectura.presentation.utils.startClearActivity
import com.etsisi.appquitectura.presentation.utils.toLabeledIntentArray

class Navigator (private val navController: NavController){

    fun openLoginDialog(config: DialogConfig) {
        val directions = LoginDirections.navigateLoginDialog(config)
        navController.navigate(directions)
    }
    fun openEditTextDialog(config: DialogConfig, dialogType: DialogType) {
        val directions = LoginFormFragmentDirections.openInputTextDialog(dialogType,config)
        navController.navigate(directions)
    }
    fun openRegisterFragment() {
        val directions = LoginFormFragmentDirections.navigateToRegisterFormFragment()
        navController.navigate(directions)
    }

    fun openVerifyEmailFragment(name: String) {
        val directions = LoginDirections.navigateToEmailVerificationFragment(name)
        navController.navigate(directions)
    }

    fun navigateFromLoginToMain(activity: Activity) {
        with(activity) {
            startClearActivity<MainActivity>()
        }
    }

    fun navigateFromMainToLogin() {
        val directions = MainDirections.actionMainToLogin()
        navController.navigate(directions)
    }

    fun navigateToHome() {
        val directions = MainDirections.actionMainToHomeFragment()
        navController.navigate(directions)
    }

    fun startGame(gameModeIndex: Int, labelsSelectedIndex: IntArray?) {
        val directions = PlayFragmentDirections.actionGameModeToPlay(gameModeIndex = gameModeIndex, topicsIdSelected = labelsSelectedIndex)
        navController.navigate(directions)
    }

    fun openSection(action: ItemHomeAction) {
        val directions = when(action) {
            ItemHomeAction.ABOUT -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.ANALYTICS -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.PROFILE -> { HomeFragmentDirections.actionHomeToMyProfile() }
            ItemHomeAction.RANKING -> { HomeFragmentDirections.actionHomeToRankingFragment() }
            ItemHomeAction.CONFIGURATION -> { HomeFragmentDirections.actionHomeToSettings() }
            ItemHomeAction.START_GAME -> { HomeFragmentDirections.actionHomeToPlay() }
        }
        navController.navigate(directions)
    }

    fun openInboxMail(activity: Activity) {
        with(activity) {
            val emailIntent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"))
            val resInfo = packageManager.queryIntentActivities(emailIntent, PackageManager.MATCH_ALL)
            val intentChooser = packageManager.getLaunchIntentForPackage(resInfo.first().activityInfo.packageName)
            val openChooser = Intent.createChooser(intentChooser, "Selecciona")
            val emailApps = resInfo.toLabeledIntentArray(packageManager)

            openChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, emailApps)
            startActivity(openChooser)
        }
    }

    fun openNavigationDialog(config: DialogConfig, type: DialogType) {
        val directions = PlayFragmentDirections.openNavigationDialog(
            dialogType = type,
            config = config
        )
        navController.navigate(directions)
    }

    fun openResultFragment() {
        val directions = PlayFragmentDirections.actionPlayFragmentToResultFragment()
        navController.navigate(directions)
    }

    fun repeatIncorrectAnswers(lastScore: UserGameScoreBO) {
        val directions = PlayFragmentDirections.actionRepeatIncorrectAnswers(lastScore = lastScore)
        navController.navigate(directions)
    }

    fun onBackPressed() {
        navController.popBackStack()
    }

}