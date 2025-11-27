package com.ibl.tool.clapfindphone.main.activity

import android.content.Context
import android.content.Intent
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.databinding.ActivityHomeBinding
import com.jrm.base.BaseActivity
import com.jrm.utils.AdsHelper
import com.jrm.utils.BaseExtension
import com.jrm.utils.BaseUtils

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

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

    private fun setupViews() {
        // Setup any initial view configurations here
    }

    private fun addEvent() {
        viewBinding.imgMenu.setOnClickListener {
            BaseExtension.showActivityWithAd(this, SettingActivity::class.java, null)
        }

        viewBinding.clapBtn.setOnClickListener {
            BaseExtension.showActivityWithAd(this, ClapActivity::class.java, null)
        }

        viewBinding.antiThirefBtn.setOnClickListener {
            BaseExtension.showActivityWithAd(this, AntiTheftActivity::class.java, null)
        }

        viewBinding.antiTouchBtn.setOnClickListener {
            BaseExtension.showActivityWithAd(this, AntiTouchActivity::class.java, null)
        }
    }
}

