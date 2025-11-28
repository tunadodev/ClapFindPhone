package com.ibl.tool.clapfindphone.main.activity

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.databinding.ActivityHomeBinding
import com.ibl.tool.clapfindphone.main.antitheft.AntiTheftService
import com.ibl.tool.clapfindphone.main.antitouch.AntiTouchService
import com.ibl.tool.clapfindphone.main.clap.FeatureClapManager
import com.ibl.tool.clapfindphone.main.clap.VocalService
import com.ibl.tool.clapfindphone.main.widget.SwitchButton
import com.jrm.base.BaseActivity
import com.jrm.utils.BaseExtension
import com.jrm.utils.BaseUtils

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    // Prevent recursive toggle callbacks when we update UI programmatically
    private var isUpdatingToggles: Boolean = false

    override fun getLayoutActivity(): Int {
        return R.layout.activity_home
    }

    override fun initViews() {
        setupViews()
        addEvent()
        BaseUtils.setFinishObd(true)
        val listAdId = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = com.jrm.BuildConfig._401_home_native
                adName = AppConstants.NATIVE_HOME
            }
        )
        showRefreshNative(listAdId, AppConstants.NATIVE_HOME)
    }

    override fun onResume() {
        super.onResume()
        // When user comes back from feature screens, ensure toggles reflect real service state
        setupViews()
    }

    private fun setupViews() {
        // Sync toggle states with current running services without triggering listeners
        isUpdatingToggles = true
        viewBinding.toggleClap.setChecked(isClapServiceRunning())
        viewBinding.toggleAntiTheft.setChecked(isAntiTheftServiceRunning())
        viewBinding.toggleAntiTouch.setChecked(isAntiTouchServiceRunning())
        isUpdatingToggles = false
    }

    private fun addEvent() {
        viewBinding.imgMenu.setOnClickListener {
            BaseExtension.showActivityWithAd(this, SettingActivity::class.java, null)
        }

        // Big cards

        viewBinding.clapBtn.setOnClickListener {
            BaseExtension.showActivityWithAd(this, ClapActivity::class.java, null)
        }

        viewBinding.antiThirefBtn.setOnClickListener {
            BaseExtension.showActivityWithAd(this, AntiTheftActivity::class.java, null)
        }

        viewBinding.antiTouchBtn.setOnClickListener {
            BaseExtension.showActivityWithAd(this, AntiTouchActivity::class.java, null)
        }

        // Top toggles
        viewBinding.toggleClap.setOnCheckedChangeListener(
            object : SwitchButton.OnCheckedChangeListener {
                override fun onCheckedChanged(view: SwitchButton, isChecked: Boolean) {
                    if (isUpdatingToggles) return

                    if (isChecked) {
                        // Navigate to Clap screen; user can configure and activate there
                        BaseExtension.showActivityWithAd(
                            this@HomeActivity,
                            ClapActivity::class.java,
                            null
                        )
                    } else {
                        // Stop Clap detection immediately
                        FeatureClapManager.getInstance(this@HomeActivity).stopAll()
                        stopService(Intent(this@HomeActivity, VocalService::class.java))
                    }
                }
            }
        )

        viewBinding.toggleAntiTheft.setOnCheckedChangeListener(
            object : SwitchButton.OnCheckedChangeListener {
                override fun onCheckedChanged(view: SwitchButton, isChecked: Boolean) {
                    if (isUpdatingToggles) return

                    if (isChecked) {
                        BaseExtension.showActivityWithAd(
                            this@HomeActivity,
                            AntiTheftActivity::class.java,
                            null
                        )
                    } else {
                        // Deactivate Anti-Theft service and cancel its notification
                        FeatureClapManager.getInstance(this@HomeActivity).stopAll()
                        stopService(Intent(this@HomeActivity, AntiTheftService::class.java))
                        val notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancel(3)
                    }
                }
            }
        )

        viewBinding.toggleAntiTouch.setOnCheckedChangeListener(
            object : SwitchButton.OnCheckedChangeListener {
                override fun onCheckedChanged(view: SwitchButton, isChecked: Boolean) {
                    if (isUpdatingToggles) return

                    if (isChecked) {
                        BaseExtension.showActivityWithAd(
                            this@HomeActivity,
                            AntiTouchActivity::class.java,
                            null
                        )
                    } else {
                        // Deactivate Anti-Touch service and cancel its notification
                        FeatureClapManager.getInstance(this@HomeActivity).stopAll()
                        stopService(Intent(this@HomeActivity, AntiTouchService::class.java))
                        val notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancel(2)
                    }
                }
            }
        )
    }

    private fun isClapServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (runningService in manager.getRunningServices(Int.MAX_VALUE)) {
            if (VocalService::class.java.name == runningService.service.className) {
                return true
            }
        }
        return false
    }

    private fun isAntiTheftServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (runningService in manager.getRunningServices(Int.MAX_VALUE)) {
            if (AntiTheftService::class.java.name == runningService.service.className) {
                return true
            }
        }
        return false
    }

    private fun isAntiTouchServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (runningService in manager.getRunningServices(Int.MAX_VALUE)) {
            if (AntiTouchService::class.java.name == runningService.service.className) {
                return true
            }
        }
        return false
    }
}

