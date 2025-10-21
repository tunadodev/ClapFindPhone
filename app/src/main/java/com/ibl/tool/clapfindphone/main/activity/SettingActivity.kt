package com.ibl.tool.clapfindphone.main.activity

import android.content.Intent
import android.view.View
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.data.preferences.SharedPrefs
import com.ibl.tool.clapfindphone.databinding.ActivitySettingBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.utils.ActionUtils
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.EventLogger
import com.ibl.tool.clapfindphone.utils.hide

class SettingActivity : BaseObdActivity<ActivitySettingBinding>()  {
    
    override fun getLayoutActivity(): Int {
        return R.layout.activity_setting
    }

    override fun initViews() {
        nameView = "setting_screen"
        setUpRate()
        setupNavigationEvents()
    }

    private fun setupNavigationEvents() {
        // Back button - navigate to Home
        viewBinding.btnBack.setOnClickListener {
            navigateToHome()
        }
        
        // Language
        viewBinding.btnLanguage.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_language")
            // TODO: Navigate to language selection
        }
        
        // Privacy Policy
        viewBinding.btnPrivacy.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_privacy")
            PolicyWebViewActivity.start(this)
        }
        
        // Rate
        viewBinding.btnRateNavigation.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_rate")
            ActionUtils.showRateDialog(this, false, callback = {
                if (it) hideRate()
            })
        }
        
        // Share
        viewBinding.btnShare.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_share")
            shareApp()
        }
        
        // Change Privacy Options (GDPR)
        // This will be shown/hidden based on SharedPrefs
        // viewBinding.btnChangePrivacy is already hidden by default in XML
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        val shareMessage = "${getString(R.string.app_name)} \nhttps://play.google.com/store/apps/details?id=${packageName}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
    }
    
    private fun navigateToHome() {
        AppExtension.showActivity(this, HomeActivity::class.java, null)
    }

    private fun hideRate() {
        viewBinding.btnRateNavigation.hide()
    }

    private fun setUpRate() {
        if (SharedPrefs.isRated(this)) {
            hideRate()
        }
    }
}