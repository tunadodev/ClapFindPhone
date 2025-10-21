package com.ibl.tool.clapfindphone.main.activity

import android.content.Context
import android.content.Intent
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.databinding.ActivityHomeBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.Utils

class HomeActivity : BaseObdActivity<ActivityHomeBinding>() {

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
        nameView = "home_screen"
        setupViews()
        addEvent()
        Utils.setFinishObd(true)
        val listAdId = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = BuildConfig._401_home_native
                adName = AppConstants.NATIVE_HOME
            }
        )
        showNative(listAdId, AppConstants.NATIVE_HOME)
        Utils.preloadInterClick(this, nameView)
    }

    private fun setupViews() {
        // Setup any initial view configurations here
    }

    private fun addEvent() {
        viewBinding.imgMenu.setOnClickListener {
            elementClickEvent("home_menu_click", "button")
            AppExtension.showActivity(this, SettingActivity::class.java, null)
        }

        viewBinding.clapBtn.setOnClickListener {
            elementClickEvent("home_clap_click", "button")
            AppExtension.showActivity(this, ClapActivity::class.java, null)
        }

        viewBinding.antiThirefBtn.setOnClickListener {
            elementClickEvent("home_anti_thief_click", "button")
            AppExtension.showActivity(this, AntiTheftActivity::class.java, null)
        }

        viewBinding.antiTouchBtn.setOnClickListener {
            elementClickEvent("home_anti_touch_click", "button")
            AppExtension.showActivity(this, AntiTouchActivity::class.java, null)
        }
    }
}

