package com.puto.tool.clapfindphone.main.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import com.puto.tool.clapfindphone.app.AppConstants
import com.puto.tool.clapfindphone.main.clap.VocalService
import com.puto.tool.clapfindphone.main.dialog.RecordPermissionDialog
import com.puto.tool.clapfindphone.utils.PermissionUtils

@Suppress("DEPRECATION")
class ClapActivity : BaseDetectionActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ClapActivity::class.java)
            context.startActivity(intent)
        }
    }

    // region BaseDetectionActivity overrides

    override val screenTitle: String
        get() = "Clap to Find Phone"

    override val nativeAdId: String
        get() = com.jrm.BuildConfig._502_clap2find_native

    override val nativeAdName: String
        get() = AppConstants.NATIVE_CLAP

    override val notificationIdToCancel: Int?
        get() = null // VocalService handles its own notification id

    override fun isDetectionServiceRunning(): Boolean {
        for (runningServiceInfo in (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
            Int.MAX_VALUE
        )) {
            if (VocalService::class.java.name == runningServiceInfo.service.className) {
                return true
            }
        }
        return false
    }

    override fun startDetectionServiceImpl() {
        startForegroundService(this, Intent(this, VocalService::class.java))
    }

    override fun stopDetectionServiceImpl() {
        stopService(Intent(this, VocalService::class.java))
    }

    override fun onSoundSettingsClicked() {
        // Clap screen opens settings with ads
        com.jrm.utils.BaseExtension.showActivityWithAd(this, SettingActivity::class.java, null)
    }

    override fun onActivateRequested() {
        // Step 1: Check microphone permission (REQUIRED)
        requestMicrophonePermission()
    }

    // endregion

    private fun requestMicrophonePermission() {
        if (!PermissionUtils.checkMicroPermission(this)) {
            // Show custom dialog first
            RecordPermissionDialog(this) { allow ->
                if (allow) {
                    // User clicked Allow in custom dialog, now request system permission
                    PermissionUtils.requestMicroPermission(this)
                } else {
                    // User clicked Deny in custom dialog
                }
            }.setDialogCancellable(false).show()
        } else {
            // Microphone permission granted, move to Step 2
            requestNotificationPermissionAndStart()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            com.puto.tool.clapfindphone.REQUEST_MICRO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    // Microphone permission granted, move to Step 2
                    requestNotificationPermissionAndStart()
                } else {
                    // Microphone permission denied, show dialog again or go to settings
                    showMicrophonePermissionDeniedDialog()
                }
            }
            com.puto.tool.clapfindphone.REQUEST_NOTIFICATION_PERMISSION_CODE -> {
                // Notification permission result doesn't matter, service already started via base
                if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }
    
    private fun showMicrophonePermissionDeniedDialog() {
        // Show dialog to ask user again or go to settings
        RecordPermissionDialog(this) { allow ->
            if (allow) {
                // Check if should show rationale or go to settings
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)) {
                    // User can still be asked for permission
                    PermissionUtils.requestMicroPermission(this)
                } else {
                    // User has permanently denied, need to go to settings
                    PermissionUtils.goSettingsForMicroPermission(this)
                }
            } else {
            }
        }.setDialogCancellable(false).show()
    }
}