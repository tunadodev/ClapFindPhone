package com.ibl.tool.clapfindphone.navigation

import android.content.Context
import android.content.Intent
import com.ibl.tool.clapfindphone.main.activity.HomeActivity
import com.jrm.onboarding.navigation.BaseNavigator

class AppNavigatorImpl : BaseNavigator {
    override fun navigateToHome(context: Context) {
        val intent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}
