package com.ibl.tool.clapfindphone.main.activity

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioManager
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startForegroundService
import androidx.recyclerview.widget.GridLayoutManager
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.bumptech.glide.Glide
import com.ibl.tool.clapfindphone.ACTION_FINISH_DETECT
import com.ibl.tool.clapfindphone.ACTION_NOTIFICATION_CLICKED_SERVICE
import com.ibl.tool.clapfindphone.ADAPTER_ADS_TYPE
import com.ibl.tool.clapfindphone.ADAPTER_ITEM_TYPE
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.data.model.SoundItem
import com.ibl.tool.clapfindphone.data.repo.AppRepository
import com.ibl.tool.clapfindphone.databinding.ActivityDetectionCommonBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.main.adapter.ClapSoundAdapter
import com.ibl.tool.clapfindphone.main.clap.ClassesApp
import com.ibl.tool.clapfindphone.main.clap.FeatureClapManager
import com.ibl.tool.clapfindphone.main.clap.VocalService
import com.ibl.tool.clapfindphone.main.dialog.NotificationPermissionDialog
import com.ibl.tool.clapfindphone.main.dialog.RecordPermissionDialog
import com.ibl.tool.clapfindphone.main.dialog.SelectSoundDialog
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.BroadcastUtils
import com.ibl.tool.clapfindphone.utils.PermissionUtils
import com.ibl.tool.clapfindphone.utils.app.AppPreferences
import java.util.Locale

@Suppress("DEPRECATION")
class ClapActivity : BaseObdActivity<ActivityDetectionCommonBinding>() {
    
    private var classesApp: ClassesApp? = null
    private lateinit var currentSoundItem: SoundItem
    private var intOnTick = 0
    private var isActive = false
    private lateinit var soundAdapter: ClapSoundAdapter
    
    private val finishDetectReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            deactivateDetection()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ClapActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutActivity(): Int {
        return R.layout.activity_detection_common
    }

    override fun initViews() {
        nameView = "clap_screen"
        classesApp = ClassesApp(this)
        
        // Set title for Clap
        viewBinding.textTitle.text = "Clap to Find Phone"
        
        BroadcastUtils.registerReceiver(
            this,
            finishDetectReceiver,
            IntentFilter(ACTION_FINISH_DETECT)
        )
        
        // Check if clicked from notification
        if (intent?.action == ACTION_NOTIFICATION_CLICKED_SERVICE) {
            logEvent("click_noti_deactivate_clap")
            deactivateDetection()
        }
        
        // Check if service is running
        if (isMyServiceRunning()) {
            setActiveUI()
        } else {
            setInactiveUI()
        }
        
        setupSoundList()
        settingSound()
        checkAndShowGuide()
        addEvent()
        val listAdId = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = BuildConfig._502_clap2find_native
                adName = AppConstants.NATIVE_CLAP
            }
        )
        showNative(listAdId, AppConstants.NATIVE_CLAP, R.layout.custom_native_admob_small)
        showRefreshNativeBanner()
    }

    private fun setupSoundList() {
        val soundList = AppRepository.getAllSound(this)
        soundAdapter = ClapSoundAdapter(soundList, this) { soundItem ->
            onSoundItemClick(soundItem)
        }
        
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (soundAdapter.getItemViewType(position)) {
                    ADAPTER_ADS_TYPE -> 3
                    ADAPTER_ITEM_TYPE -> 1
                    else -> 1
                }
            }
        }
        
        viewBinding.rcvSounds.layoutManager = gridLayoutManager
        viewBinding.rcvSounds.adapter = soundAdapter
    }

    private fun onSoundItemClick(soundItem: SoundItem) {
//        logEvent("clap_sound_select_${soundItem.name?.replace(" ", "_")?.toLowerCase()}")
        
        // Hide guide when user clicks on a sound
        soundAdapter.setShowGuide(false)
        
        // Open sound settings activity
        SoundSettingsActivity.start(this, soundItem)
    }

    private fun addEvent() {
        viewBinding.btnBack.setOnClickListener {
            logEvent("clap_back_click")
            navigateToHome()
        }
        
//        viewBinding.btnHelp.setOnClickListener {
//            logEvent("clap_help_click")
//            startActivity(Intent(this, HowToUseActivity::class.java))
//        }
        
        viewBinding.btnSoundSettings.setOnClickListener {
            logEvent("clap_sound_settings_click")
            AppExtension.showActivity(this, SettingActivity::class.java, null)

        }
        
        viewBinding.ivActivateButton.setOnClickListener {
            if (!isActive) {
                activateDetection()
            } else {
                deactivateDetection()
            }
        }
        
        viewBinding.clButtonContainer.setOnClickListener {
            if (!isActive) {
                activateDetection()
            } else {
                deactivateDetection()
            }
        }
    }

    private fun activateDetection() {
        // Check if user has selected a sound
        if (!AppPreferences.instance.hasSelectedSound) {
            logEvent("clap_activate_without_sound")
            showSelectSoundDialog()
            return
        }
        
        // Step 1: Check microphone permission (REQUIRED)
        requestMicrophonePermission()
    }
    
    private fun requestMicrophonePermission() {
        if (!PermissionUtils.checkMicroPermission(this)) {
            logEvent("clap_request_micro_permission")
            // Show custom dialog first
            RecordPermissionDialog(this) { allow ->
                if (allow) {
                    // User clicked Allow in custom dialog, now request system permission
                    PermissionUtils.requestMicroPermission(this)
                } else {
                    // User clicked Deny in custom dialog
                    logEvent("clap_micro_permission_denied_dialog")
                }
            }.setDialogCancellable(false).show()
        } else {
            // Microphone permission granted, move to Step 2
            requestNotificationPermission()
        }
    }
    
    private fun requestNotificationPermission() {
        if (!PermissionUtils.checkNotificationPermission(this)) {
            logEvent("clap_request_notification_permission")
            // Show custom dialog first
            NotificationPermissionDialog(this) { allow ->
                if (allow) {
                    // User clicked Allow in custom dialog, request system permission
                    PermissionUtils.requestNotificationPermission(this)
                    // Continue to activate regardless of system permission result
                    startDetectionService()
                } else {
                    // User clicked Deny in custom dialog, still activate
                    logEvent("clap_notification_permission_denied_dialog")
                    startDetectionService()
                }
            }.setDialogCancellable(false).show()
        } else {
            // Notification permission already granted, activate
            startDetectionService()
        }
    }
    
    private fun startDetectionService() {
        logEvent("clap_activate_click")
        isActive = true
        setActiveUI()
        
        classesApp?.save("StopService", "0")
        startForegroundService(this, Intent(this, VocalService::class.java))
    }

    private fun deactivateDetection() {
        logEvent("clap_deactivate_click")
        isActive = false
        setInactiveUI()
        
        FeatureClapManager.getInstance(this).stopAll()
        stopService(Intent(this, VocalService::class.java))
    }

    private fun setActiveUI() {
        viewBinding.ivActivateButton.setImageResource(R.drawable.active)
        viewBinding.tvStatus.text = "Activated"
        viewBinding.tvStatus.setTextColor(Color.parseColor("#00C853"))
    }

    private fun setInactiveUI() {
        viewBinding.ivActivateButton.setImageResource(R.drawable.deactive)
        viewBinding.tvStatus.text = "Deactivated"
        viewBinding.tvStatus.setTextColor(Color.parseColor("#404040"))
    }

    private fun isMyServiceRunning(): Boolean {
        for (runningServiceInfo in (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
            Int.MAX_VALUE
        )) {
            if (VocalService::class.java.name == runningServiceInfo.service.className) {
                return true
            }
        }
        return false
    }

    private fun initVolume() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        audioManager?.let {
            it.getStreamVolume(3)
            it.setStreamVolume(
                3,
                (it.getStreamMaxVolume(3).toFloat() * 
                    (classesApp?.read(androidx.core.app.NotificationCompat.CATEGORY_PROGRESS, "50")?.toFloat() ?: 50f / 100.0f)).toInt(),
                0
            )
        }
    }

    private fun settingSound() {
        currentSoundItem = AppPreferences.instance.currentSound
        initVolume()
        object : CountDownTimer(5000, 500) {
            override fun onTick(j: Long) {
                intOnTick += 1
            }

            override fun onFinish() {
                intOnTick = 0
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        // Refresh selected sound when returning from settings
        if (AppPreferences.instance.hasSelectedSound) {
            currentSoundItem = AppPreferences.instance.currentSound
            soundAdapter.setSelectedSound(currentSoundItem)
        }
        
        // Check and hide guide if user has selected sound
        checkAndShowGuide()
    }
    
    private fun checkAndShowGuide() {
        val shouldShowGuide = !AppPreferences.instance.hasSelectedSound
        soundAdapter.setShowGuide(shouldShowGuide)
        if (shouldShowGuide) {
            logEvent("clap_guide_shown")
        }
    }
    
    private fun showSelectSoundDialog() {
        SelectSoundDialog(this) {
            // Do nothing, just close dialog
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(finishDetectReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            com.ibl.tool.clapfindphone.REQUEST_MICRO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    // Microphone permission granted, move to Step 2
                    logEvent("clap_micro_permission_granted")
                    requestNotificationPermission()
                } else {
                    // Microphone permission denied, show dialog again or go to settings
                    logEvent("clap_micro_permission_denied_system")
                    showMicrophonePermissionDeniedDialog()
                }
            }
            com.ibl.tool.clapfindphone.REQUEST_NOTIFICATION_PERMISSION_CODE -> {
                // Notification permission result doesn't matter, service already started
                if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    logEvent("clap_notification_permission_granted")
                } else {
                    logEvent("clap_notification_permission_denied_system")
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
                    logEvent("clap_micro_permission_go_settings")
                    PermissionUtils.goSettingsForMicroPermission(this)
                }
            } else {
                logEvent("clap_micro_permission_cancelled")
            }
        }.setDialogCancellable(false).show()
    }

    override fun onBackPressed() {
        AppExtension.showActivity(this, HomeActivity::class.java, null)
    }
    
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}

