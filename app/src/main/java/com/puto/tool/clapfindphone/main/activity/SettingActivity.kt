package com.puto.tool.clapfindphone.main.activity

import android.content.Intent
import com.puto.tool.clapfindphone.R
import com.puto.tool.clapfindphone.data.preferences.SharedPrefs
import com.puto.tool.clapfindphone.databinding.ActivitySettingBinding
import com.puto.tool.clapfindphone.utils.ActionUtils
import com.puto.tool.clapfindphone.utils.hide
import com.jrm.base.BaseActivity
import com.jrm.utils.BaseExtension

class SettingActivity : BaseActivity<ActivitySettingBinding>()  {
    
    override fun getLayoutActivity(): Int {
        return R.layout.activity_setting
    }

    override fun initViews() {
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
        }
        
        // Privacy Policy
        viewBinding.btnPrivacy.setOnClickListener {
            PolicyWebViewActivity.start(this)
        }
        
        // Rate
        viewBinding.btnRateNavigation.setOnClickListener {
            ActionUtils.showRateDialog(this, false, callback = {
                if (it) hideRate()
            })
        }
        
        // Share
        viewBinding.btnShare.setOnClickListener {
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
        BaseExtension.showActivityWithAd(this, HomeActivity::class.java, null)
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