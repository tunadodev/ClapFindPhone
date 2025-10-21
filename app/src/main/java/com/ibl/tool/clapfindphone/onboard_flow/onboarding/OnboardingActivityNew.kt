package com.ibl.tool.clapfindphone.onboard_flow.onboarding

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.ads.nomyek_admob.ads_components.YNMAds
import com.ads.nomyek_admob.event.YNMAirBridge
import com.ads.nomyek_admob.event.YNMAirBridgeDefaultEvent
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.databinding.ActivityOnboardingScreenBinding
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.onboard_flow.ViewPagerAddFragmentsAdapter
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity

class OnboardingActivityNew : BaseObdActivity<ActivityOnboardingScreenBinding>() {
    override fun getLayoutActivity(): Int {
        return R.layout.activity_onboarding_screen
    }

    override fun initViews() {
        nameView = "Ob"
        initViewPager()
    }
    private fun initViewPager() {
        val adapter = ViewPagerAddFragmentsAdapter(supportFragmentManager, lifecycle)
        var index: Int = 0;
        adapter.addFrag(OnboardingFragmentNew(index++, R.drawable.obd1, R.string.obd_title1, R.string.obd_detail1))

        viewBinding.viewpagerOnboard.adapter = adapter
        viewBinding.indicatorView.visibility = View.INVISIBLE

        viewBinding.viewpagerOnboard.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewBinding.indicatorView.selection = position
                viewBinding.indicatorView2.selection = position
                showIndicatorView(position)
                Log.d("Onboarding", "onPageSelected: $position")
                when (position) {
                    0 -> {
                        YNMAirBridgeDefaultEvent.pushEventScreenView(YNMAirBridge.AppData("Ob1", ""))
                    }
                }
            }
        })

        viewBinding.tvNext.setOnClickListener {
            onClickNext()
        }
        viewBinding.tvNext2.setOnClickListener {
            onClickNext()
        }
        viewBinding.btnContinue.setOnClickListener {
            onClickNext()
        }
    }

    public fun onClickNext() {
        if (viewBinding.viewpagerOnboard.currentItem == viewBinding.viewpagerOnboard.adapter!!.itemCount - 1) {
            goToNextActivity()
        } else {
            viewBinding.viewpagerOnboard.currentItem = viewBinding.viewpagerOnboard.currentItem + 1
        }
    }

    private fun goToNextActivity() {
        goHome()
    }
    private fun goHome() {
        YNMAds.getInstance().adConfig.setInterFlow(RemoteConfigManager.instance!!.getStartIndexInter(), RemoteConfigManager.instance!!.getDeltaIndexInter())
        YNMAds.isGoHome = true;
        AppExtension.goHomeActivity(this, null)
        finish()
    }

    public fun highlighNext() {
        viewBinding.tvNext.setTextColor(ContextCompat.getColor(this, R.color.obd_theme_color))
    }

    fun showIndicatorView(pos: Int){
        viewBinding.tvNext.text = resources.getString(R.string.start)
//        viewBinding.tvNext2.visibility = View.GONE
//        viewBinding.tvNext.setTextColor(if (pos == getLastPos())
//            ContextCompat.getColor(this, R.color.obd_theme_color)
//            else ContextCompat.getColor(this, com.ads.nomyek_admob.R.color.textColor))
//        viewBinding.tvNext.text = (if (pos == getLastPos()) resources.getString(R.string.start) else resources.getString(R.string.next))
//        if (pos in getListPosNativeNormal()) {
//            viewBinding.indicatorView.visibility = View.VISIBLE
//            viewBinding.tvNext.visibility = View.VISIBLE
//            viewBinding.indicatorView2.visibility = View.INVISIBLE
//            viewBinding.layoutContinue2.visibility = View.INVISIBLE
//        }
//        if (pos in getListNoNative()) {
//            viewBinding.indicatorView.visibility = View.INVISIBLE
//            viewBinding.tvNext.visibility = View.INVISIBLE
//            viewBinding.indicatorView2.visibility = View.VISIBLE
//            viewBinding.layoutContinue2.visibility = View.VISIBLE
//
//        }
//        if (pos in getListPosNativeFull()) {
//            viewBinding.indicatorView.visibility = View.INVISIBLE
//            viewBinding.tvNext.visibility = View.INVISIBLE
//            viewBinding.indicatorView2.visibility = View.INVISIBLE
//            viewBinding.layoutContinue2.visibility = View.INVISIBLE
//            viewBinding.tvNext2.visibility = View.VISIBLE
//        }
    }

}
