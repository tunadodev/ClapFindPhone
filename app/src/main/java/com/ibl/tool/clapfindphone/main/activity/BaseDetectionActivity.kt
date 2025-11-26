package com.ibl.tool.clapfindphone.main.activity

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.graphics.toColorInt
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
import com.ibl.tool.clapfindphone.data.repo.AppRepository
import com.ibl.tool.clapfindphone.databinding.ActivityDetectionCommonBinding
import com.ibl.tool.clapfindphone.main.adapter.ClapSoundAdapter
import com.ibl.tool.clapfindphone.main.clap.ClassesApp
import com.ibl.tool.clapfindphone.main.clap.FeatureClapManager
import com.ibl.tool.clapfindphone.main.dialog.NotificationPermissionDialog
import com.ibl.tool.clapfindphone.main.dialog.SelectSoundDialog
import com.ibl.tool.clapfindphone.utils.BroadcastUtils
import com.ibl.tool.clapfindphone.utils.PermissionUtils
import com.ibl.tool.clapfindphone.utils.app.AppPreferences
import com.jrm.base.BaseActivity
import com.jrm.utils.BaseExtension

/**
 * Common base screen for Clap / Anti-Theft / Anti-Touch detection flows.
 * Shares UI, sound list, activation flow and notification-permission logic.
 */
abstract class BaseDetectionActivity : BaseActivity<ActivityDetectionCommonBinding>() {
    protected var classesApp: ClassesApp? = null
    protected lateinit var currentSoundItem: SoundItem
    protected var intOnTick: Int = 0
    protected var isActive: Boolean = false
    protected lateinit var soundAdapter: ClapSoundAdapter

    /** Title shown in toolbar. */
    protected abstract val screenTitle: String

    /** Native ad configuration for this screen. */
    protected abstract val nativeAdId: String
    protected abstract val nativeAdName: String

    /** Subclasses provide their detection service status & lifecycle. */
    protected abstract fun isDetectionServiceRunning(): Boolean
    protected abstract fun startDetectionServiceImpl()
    protected abstract fun stopDetectionServiceImpl()

    /** Optional notification id to cancel when deactivating (AntiTheft / AntiTouch). */
    protected open val notificationIdToCancel: Int? = null

    private val finishDetectReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            deactivateDetection()
        }
    }

    override fun getLayoutActivity(): Int = R.layout.activity_detection_common

    override fun initViews() {
        classesApp = ClassesApp(this)

        // Title
        viewBinding.textTitle.text = screenTitle

        // Finish-detection broadcast
        BroadcastUtils.registerReceiver(
            this,
            finishDetectReceiver,
            IntentFilter(ACTION_FINISH_DETECT)
        )

        // Handle notification click vs normal open
        if (intent?.action == ACTION_NOTIFICATION_CLICKED_SERVICE) {
            deactivateDetection()
        } else {
            if (isDetectionServiceRunning()) {
                isActive = true
                setActiveUI()
            } else {
                isActive = false
                setInactiveUI()
            }
        }

        setupSoundList()
        settingSound()
        checkAndShowGuide()
        addEvent()
        setupNativeAds()
    }

    private fun setupNativeAds() {
        val listAdId = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = nativeAdId
                adName = nativeAdName
            }
        )
        showRefreshNative(listAdId, nativeAdName, "nativeAd", R.layout.custom_native_admob_small)
        showRefreshNativeBanner()
    }

    protected open fun addEvent() {
        viewBinding.btnBack.setOnClickListener {
            navigateToHome()
        }

        viewBinding.btnSoundSettings.setOnClickListener {
            onSoundSettingsClicked()
        }

        val toggleListener = View.OnClickListener {
            if (!isActive) {
                activateDetection()
            } else {
                deactivateDetection()
            }
        }
        viewBinding.ivActivateButton.setOnClickListener(toggleListener)
        viewBinding.clButtonContainer.setOnClickListener(toggleListener)
    }

    /**
     * Subclasses can override if they need a different Settings navigation.
     */
    protected open fun onSoundSettingsClicked() {
        startActivity(Intent(this, SettingActivity::class.java))
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
        // Hide guide when user clicks on a sound
        soundAdapter.setShowGuide(false)

        // Open sound settings activity
        SoundSettingsActivity.start(this, soundItem)
    }

    private fun activateDetection() {
        // Require that user has selected a sound
        if (!AppPreferences.instance.hasSelectedSound) {
            showSelectSoundDialog()
            return
        }

        onActivateRequested()
    }

    /**
     * Entry point for subclasses to run their permission flow before starting service.
     */
    protected abstract fun onActivateRequested()

    /**
     * Shared notification-permission flow used by all 3 features.
     * Call this from subclasses once all other required permissions are granted.
     */
    protected fun requestNotificationPermissionAndStart() {
        if (!PermissionUtils.checkNotificationPermission(this)) {
            NotificationPermissionDialog(this) { allow ->
                if (allow) {
                    PermissionUtils.requestNotificationPermission(this)
                    // Continue to activate regardless of system permission result
                    startDetectionService()
                } else {
                    // User clicked Deny in custom dialog, still activate
                    startDetectionService()
                }
            }.setDialogCancellable(false).show()
        } else {
            // Notification permission already granted, activate
            startDetectionService()
        }
    }

    /**
     * Called by subclasses once all permissions have been granted.
     */
    protected fun startDetectionService() {
        isActive = true
        setActiveUI()

        classesApp?.save("StopService", "0")

        try {
            startDetectionServiceImpl()
        } catch (e: Exception) {
            Log.e("BaseDetectionActivity", "Error starting service", e)
            // If service can't start, revert UI
            isActive = false
            setInactiveUI()

            android.widget.Toast.makeText(
                this,
                "Cannot start service. Please try again.",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    protected fun deactivateDetection() {
        isActive = false
        setInactiveUI()

        // Stop all sounds, flash, vibration
        FeatureClapManager.getInstance(this).stopAll()

        // Stop the service
        stopDetectionServiceImpl()

        // Cancel notification if provided
        notificationIdToCancel?.let { id ->
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(id)
        }
    }

    protected fun setActiveUI() {
        viewBinding.ivActivateButton.setImageResource(R.drawable.active)
        viewBinding.tvStatus.text = "Activated"
        viewBinding.tvStatus.setTextColor("#11D0DD".toColorInt())
    }

    protected fun setInactiveUI() {
        viewBinding.ivActivateButton.setImageResource(R.drawable.deactive)
        viewBinding.tvStatus.text = "Deactivated"
        viewBinding.tvStatus.setTextColor("#FDFDFD".toColorInt())
    }

    private fun initVolume() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        audioManager?.let {
            it.getStreamVolume(3)
            it.setStreamVolume(
                3,
                (it.getStreamMaxVolume(3).toFloat() *
                    (classesApp?.read(
                        androidx.core.app.NotificationCompat.CATEGORY_PROGRESS,
                        "50"
                    )?.toFloat() ?: 50f / 100.0f)).toInt(),
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

        // Keep UI in sync with service state
        if (!isDetectionServiceRunning()) {
            isActive = false
            setInactiveUI()
            // Make sure to stop any playing sound
            FeatureClapManager.getInstance(this).stopAll()
        } else {
            isActive = true
            setActiveUI()
        }
    }

    private fun checkAndShowGuide() {
        val shouldShowGuide = !AppPreferences.instance.hasSelectedSound
        soundAdapter.setShowGuide(shouldShowGuide)
    }

    private fun showSelectSoundDialog() {
        SelectSoundDialog(this) {
            // Do nothing, just close dialog
        }.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        BaseExtension.showActivityWithAd(this, HomeActivity::class.java, null)
    }

    protected fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(finishDetectReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



