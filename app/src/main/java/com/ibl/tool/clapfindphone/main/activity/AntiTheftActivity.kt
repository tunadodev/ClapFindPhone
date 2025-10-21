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
import com.ibl.tool.clapfindphone.ACTION_FINISH_DETECT
import com.ibl.tool.clapfindphone.ACTION_NOTIFICATION_CLICKED_SERVICE
import com.ibl.tool.clapfindphone.ADAPTER_ADS_TYPE
import com.ibl.tool.clapfindphone.ADAPTER_ITEM_TYPE
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.data.model.SoundItem
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.data.repo.AppRepository
import com.ibl.tool.clapfindphone.databinding.ActivityDetectionCommonBinding
import com.ibl.tool.clapfindphone.main.adapter.ClapSoundAdapter
import com.ibl.tool.clapfindphone.main.antitheft.AntiTheftService
import com.ibl.tool.clapfindphone.main.clap.ClassesApp
import com.ibl.tool.clapfindphone.main.clap.FeatureClapManager
import com.ibl.tool.clapfindphone.main.dialog.SelectSoundDialog
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.BroadcastUtils
import com.ibl.tool.clapfindphone.utils.PermissionUtils
import com.ibl.tool.clapfindphone.utils.app.AppPreferences
import java.util.Locale

@Suppress("DEPRECATION")
class AntiTheftActivity : BaseObdActivity<ActivityDetectionCommonBinding>() {
    
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
            val intent = Intent(context, AntiTheftActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutActivity(): Int {
        return R.layout.activity_detection_common
    }

    override fun initViews() {
        nameView = "antitheft_screen"
        classesApp = ClassesApp(this)
        
        // Set title for Anti Theft
        viewBinding.textTitle.text = "Anti Theft"
        
        BroadcastUtils.registerReceiver(
            this,
            finishDetectReceiver,
            IntentFilter(ACTION_FINISH_DETECT)
        )
        
        // Check if clicked from notification
        if (intent?.action == ACTION_NOTIFICATION_CLICKED_SERVICE) {
            logEvent("click_noti_deactivate_antitheft")
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
                adId = BuildConfig._503_antithief_native
                adName = AppConstants.NATIVE_ANTI_THIEF
            }
        )
        showNative(listAdId, AppConstants.NATIVE_ANTI_THIEF, R.layout.custom_native_admob_small)
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
//        logEvent("antitheft_sound_select_${soundItem.name?.replace(" ", "_")?.toLowerCase()}")
        
        // Hide guide when user clicks on a sound
        soundAdapter.setShowGuide(false)
        
        // Open sound settings activity
        SoundSettingsActivity.start(this, soundItem)
    }

    private fun addEvent() {
        viewBinding.btnBack.setOnClickListener {
            logEvent("antitheft_back_click")
            navigateToHome()
        }
        
//        viewBinding.btnHelp.setOnClickListener {
//            logEvent("antitheft_help_click")
//            startActivity(Intent(this, HowToUseActivity::class.java))
//        }
        
        viewBinding.btnSoundSettings.setOnClickListener {
            logEvent("antitheft_sound_settings_click")
            startActivity(Intent(this, SettingActivity::class.java))
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
            logEvent("antitheft_activate_without_sound")
            showSelectSoundDialog()
            return
        }
        
        // Check notification permission
        checkPermissionNotification()
        
        if (PermissionUtils.checkNotificationPermission(this)) {
            logEvent("antitheft_activate_click")
            isActive = true
            setActiveUI()
            
            classesApp?.save("StopService", "0")
            startForegroundService(this, Intent(this, AntiTheftService::class.java))
        }
    }
    
    private fun checkPermissionNotification() {
        if (!PermissionUtils.checkNotificationPermission(this)) {
            PermissionUtils.requestNotificationPermission(this)
        }
    }

    private fun deactivateDetection() {
        logEvent("antitheft_deactivate_click")
        isActive = false
        setInactiveUI()
        
        // Force stop all sounds, flash, vibration
        FeatureClapManager.getInstance(this).stopAll()
        
        // Stop the service
        stopService(Intent(this, AntiTheftService::class.java))
        
        // Make sure notification is cancelled
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.cancel(3) // NOTIFICATION_ID = 3 from AntiTheftService
        
        Log.d("AntiTheftActivity", "Deactivated - service stopped, sound stopped, notification cancelled")
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
            if (AntiTheftService::class.java.name == runningServiceInfo.service.className) {
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
        
        // Check if service is still running and update UI accordingly
        if (!isMyServiceRunning()) {
            isActive = false
            setInactiveUI()
            // Make sure to stop any playing sound
            FeatureClapManager.getInstance(this).stopAll()
        } else {
            isActive = true
            setActiveUI()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(finishDetectReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
    
    private fun checkAndShowGuide() {
        val shouldShowGuide = !AppPreferences.instance.hasSelectedSound
        soundAdapter.setShowGuide(shouldShowGuide)
        if (shouldShowGuide) {
            logEvent("antitheft_guide_shown")
        }
    }
    
    private fun showSelectSoundDialog() {
        SelectSoundDialog(this) {
            // Do nothing, just close dialog
        }.show()
    }
}


