package com.ibl.tool.clapfindphone.main.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.main.antitouch.AntiTouchService

@Suppress("DEPRECATION")
class AntiTouchActivity : BaseDetectionActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AntiTouchActivity::class.java)
            context.startActivity(intent)
        }
    }

    // region BaseDetectionActivity overrides

    override val screenTitle: String
        get() = "Anti Touch"

    override val nativeAdId: String
        get() = BuildConfig._504_antitouch_native

    override val nativeAdName: String
        get() = AppConstants.NATIVE_ANTI_TOUCH

    override val notificationIdToCancel: Int
        get() = 2 // NOTIFICATION_ID from AntiTouchService

    override fun isDetectionServiceRunning(): Boolean {
        for (runningServiceInfo in (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
            Int.MAX_VALUE
        )) {
            if (AntiTouchService::class.java.name == runningServiceInfo.service.className) {
                return true
            }
        }
        return false
    }

    override fun startDetectionServiceImpl() {
        startForegroundService(this, Intent(this, AntiTouchService::class.java))
    }

    override fun stopDetectionServiceImpl() {
        stopService(Intent(this, AntiTouchService::class.java))
    }

    override fun onActivateRequested() {
        // Only notification permission is required here
        requestNotificationPermissionAndStart()
    }

    // endregion

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            com.ibl.tool.clapfindphone.REQUEST_NOTIFICATION_PERMISSION_CODE -> {
                // Notification permission result doesn't matter, service already started via base
                if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }
}