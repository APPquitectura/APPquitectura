package com.etsisi.appquitectura.utils

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.toJson

class NavigationTracker: NavController.OnDestinationChangedListener {
    private companion object {
        const val NAV_BACK_STACK = "navigationBackStack"
        const val DESTINATION = "destination"
        const val START_DESTINATION = "startDestination"
        const val ARGUMENTS = "arguments"
    }
    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        val navBackStack = controller.backQueue.mapIndexedNotNull { index, navBackStackEntry ->
            mapOf(index to navBackStackEntry.destination.label)
        }
        val data = mapOf(
            DESTINATION to destination.label,
            START_DESTINATION to controller.graph.startDestDisplayName,
            ARGUMENTS to (arguments ?: Bundle()).toJson(),
            NAV_BACK_STACK to navBackStack
        )

        Log.v(TAG, data.toString())
    }
}
