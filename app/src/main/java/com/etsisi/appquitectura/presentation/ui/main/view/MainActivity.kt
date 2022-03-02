package com.etsisi.appquitectura.presentation.ui.main.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.presentation.utils.TAG
import com.google.firebase.auth.ActionCodeResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null && pendingDynamicLinkData.link != null) {
                    deepLink = pendingDynamicLinkData.link
                    val obbCode = pendingDynamicLinkData.link!!.getQueryParameter("obbCode").orEmpty()
                    val mode = pendingDynamicLinkData.link!!.getQueryParameter("mode").orEmpty()
                    val apiKey = pendingDynamicLinkData.link!!.getQueryParameter("apiKey").orEmpty()
                    val continueUrl = pendingDynamicLinkData.link!!.getQueryParameter("continueUrl").orEmpty()
                    Firebase
                        .auth
                        .checkActionCode(obbCode)
                        .addOnSuccessListener { result ->
                            if (result.operation == ActionCodeResult.VERIFY_EMAIL) {
                                Firebase.auth.applyActionCode(obbCode)
                                    .addOnSuccessListener {
                                        CurrentUser.instance?.reload()
                                    }
                                    .addOnFailureListener {

                                    }
                            }
                        }
                        .addOnFailureListener {

                        }
                }
            }
            .addOnFailureListener(this) { e -> Log.e(TAG, "getDynamicLink:onFailure", e) }
    }
}