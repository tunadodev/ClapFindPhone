package com.ibl.tool.clapfindphone.onboard_flow.onboarding

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.ads.nomyek_admob.ads_components.YNMAds
import com.ads.nomyek_admob.ads_components.YNMAdsCallbacks
import com.ads.nomyek_admob.ads_components.wrappers.AdsError
import com.ads.nomyek_admob.ads_components.wrappers.AdsNative
import com.ads.nomyek_admob.event.YNMAirBridge
import com.ads.nomyek_admob.event.YNMAirBridgeDefaultEvent
import com.ads.nomyek_admob.max.AppLovinCallback
import com.ads.nomyek_admob.max.MaxNativePreload
import com.ads.nomyek_admob.utils.AdsInterMultiPreload
import com.ads.nomyek_admob.utils.AdsInterPreload
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.applovin.mediation.MaxError
import com.google.android.gms.ads.nativead.NativeAd
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.app.ObdConstants
import com.ibl.tool.clapfindphone.databinding.ActivityOnboardingScreenBinding
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.onboard_flow.ViewPagerAddFragmentsAdapter
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.utils.Utils
import kotlinx.coroutines.Dispatchers

class OnboardingActivity : BaseObdActivity<ActivityOnboardingScreenBinding>() {
    var preload0b4 : Boolean = false
    var preloadOb5 : Boolean = false
    var preloadOb6 : Boolean = false

    // MutableLiveData to observe loadingOb3 state changes
    val loadingOb2LiveData = MutableLiveData<Boolean>().apply { value = true }
    override fun getLayoutActivity(): Int {
        return R.layout.activity_onboarding_screen
    }

    override fun initViews() {
        nameView = "Ob"
        initViewPager()
        preloadOnboarding2()
    }

    public fun isFiveObd() : Boolean {
        return RemoteConfigManager.instance!!.numberScreenObd.toInt() == 5;
    }

    public fun preloadOnboarding2() {
        if (!Utils.isFinishObd()) {
            if (RemoteConfigManager.instance!!.getListAdIsOrder303().get(0).equals("Max")) {
                MaxNativePreload.getInstance().preloadNative(this, RemoteConfigManager.instance!!.getOb2MaxAdId(),
                    ObdConstants.PRELOAD_NATIVE_303_1,R.layout.custom_full_screen_native_ads_max, object : AppLovinCallback() {
                        override fun onAdFailedToLoad(i: MaxError?) {
                            super.onAdFailedToLoad(i)
                            val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                                AdsNativeMultiPreload.AdIdModel().apply {
                                    adId = RemoteConfigManager.instance!!.getOb2HighAdId()
                                    adName = "native_onboard_2_high"
                                },
                                AdsNativeMultiPreload.AdIdModel().apply {
                                    adId = RemoteConfigManager.instance!!.getOb2AdId()
                                    adName = "native_onboard_2"
                                }
                            )

                            AdsNativeMultiPreload.preloadMultipleNativeAds(
                                this@OnboardingActivity,
                                YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_ONBOARD_2),
                                listAdId,
                                ObdConstants.PRELOAD_NATIVE_303_2,
                                object : YNMAdsCallbacks() {
                                    override fun onAdClicked() {
                                        super.onAdClicked()
                                        OnboardingFragment.isClickNativeFull = true;
                                    }

                                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                                        super.onNativeAdLoaded(nativeAd)
                                        loadingOb2LiveData.postValue(false)
                                    }
                                }
                            )
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            OnboardingFragment.isClickNativeFull = true;
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            loadingOb2LiveData.postValue(false)
                        }
                    }
                )
            } else {
                val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                    AdsNativeMultiPreload.AdIdModel().apply {
                        adId = RemoteConfigManager.instance!!.getOb2HighAdId()
                        adName = "native_onboard_2_high"
                    }
                )

                AdsNativeMultiPreload.preloadMultipleNativeAds(
                    this@OnboardingActivity,
                    YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_ONBOARD_2),
                    listAdId,
                    ObdConstants.PRELOAD_NATIVE_303_1,
                    object : YNMAdsCallbacks() {
                        override fun onAdClicked() {
                            super.onAdClicked()
                            OnboardingFragment.isClickNativeFull = true;
                        }

                        override fun onNativeAdLoaded(nativeAd: NativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            loadingOb2LiveData.postValue(false)
                        }

                        override fun onAdFailedToLoad(adError: AdsError?) {
                            super.onAdFailedToLoad(adError)
                            MaxNativePreload.getInstance().preloadNative(this@OnboardingActivity, RemoteConfigManager.instance!!.getOb2MaxAdId(),
                                ObdConstants.PRELOAD_NATIVE_303_2,R.layout.custom_full_screen_native_ads, object : AppLovinCallback() {
                                    override fun onAdFailedToLoad(i: MaxError?) {
                                        super.onAdFailedToLoad(i)
                                        val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                                            AdsNativeMultiPreload.AdIdModel().apply {
                                                adId = RemoteConfigManager.instance!!.getOb2AdId()
                                                adName = "native_onboard_2"
                                            }
                                        )

                                        AdsNativeMultiPreload.preloadMultipleNativeAds(
                                            this@OnboardingActivity,
                                            YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_ONBOARD_2),
                                            listAdId,
                                            ObdConstants.PRELOAD_NATIVE_303_3,
                                            object : YNMAdsCallbacks() {
                                                override fun onAdClicked() {
                                                    super.onAdClicked()
                                                    OnboardingFragment.isClickNativeFull = true;
                                                }

                                                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                                                    super.onNativeAdLoaded(nativeAd)
                                                    loadingOb2LiveData.postValue(false)
                                                }
                                            }
                                        )
                                    }

                                    override fun onAdClicked() {
                                        super.onAdClicked()
                                        OnboardingFragment.isClickNativeFull = true;
                                    }

                                    override fun onAdLoaded() {
                                        super.onAdLoaded()
                                        loadingOb2LiveData.postValue(false)
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    }

    private fun preloadOnboarding4() {
        preload0b4 = true;
        if (!Utils.isFinishObd()) {
            val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb4HighAdId()
                    adName = "native_onboard_4_high"
                },
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb4AdId()
                    adName = "native_onboard_4"
                }
            )

            AdsNativeMultiPreload.preloadMultipleNativeAds(
                this@OnboardingActivity,
                YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_ONBOARD_4),
                listAdId,
                ObdConstants.NATIVE_ONBOARD_4,
                object : YNMAdsCallbacks() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                        if (isFiveObd()) {
                            OnboardingFragment.isClickNativeFull = true;
                        }
                    }
                }
            )
        }
    }

    private fun preloadOnboarding5() {
        preloadOb5 = true;
        if (!Utils.isFinishObd()) {
            val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb5HighAdId()
                    adName = "native_onboard_5_high"
                },
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb5AdId()
                    adName = "native_onboard_5"
                }
            )

            AdsNativeMultiPreload.preloadMultipleNativeAds(
                this@OnboardingActivity,
                YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_ONBOARD_5),
                listAdId,
                ObdConstants.NATIVE_ONBOARD_5,
                object : YNMAdsCallbacks() {
                }
            )
        }
    }

    private fun preloadOnboarding6() {
        preloadOb6 = true;
        if (!Utils.isFinishObd()) {
            val listAdId: List<AdsInterMultiPreload.AdIdModel> = listOf(
                AdsInterMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb6HighAdId()
                    adName = "inter_odb6_high"
                },
                AdsInterMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb6AdId()
                    adName = "inter_odb6"
                }
            )
            AdsInterMultiPreload.preloadMultipleInterAds(
                this@OnboardingActivity,
                YNMAirBridge.AppData(nameView, "inter_odb6"),
                listAdId,
                ObdConstants.INTER_ONBOARD_6,
                object : YNMAdsCallbacks(YNMAirBridge.AppData(nameView, ObdConstants.INTER_ONBOARD_6), YNMAds.INTERSTITIAL) {
                }
            )
        }
    }
    private fun initViewPager() {
        val adapter = ViewPagerAddFragmentsAdapter(supportFragmentManager, lifecycle)
        var index: Int = 0;
        adapter.addFrag( OnboardingFragment(index++, R.drawable.obd1, R.string.obd_title1, R.string.obd_detail1))
        if (isFiveObd()) {
            adapter.addFrag(OnboardingFragment(index++, R.drawable.obd1, R.string.obd_title1, R.string.obd_title3))
        }
        adapter.addFrag(OnboardingFragment(index++, R.drawable.obd2, R.string.obd_title2, R.string.obd_detail2))
        adapter.addFrag(OnboardingFragment(index++, R.drawable.obd1, R.string.obd_title1, R.string.obd_title3))
        adapter.addFrag(OnboardingFragment(index++, R.drawable.obd3, R.string.obd_title3, R.string.obd_detail3))

        viewBinding.viewpagerOnboard.adapter = adapter
        if (isFiveObd()) {
            viewBinding.indicatorView.count = 5
            viewBinding.indicatorView2.count = 5
        }

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
                    1 -> {
                        YNMAirBridgeDefaultEvent.pushEventScreenView(YNMAirBridge.AppData("Ob2", ""))
                        if (!preload0b4 && !isFiveObd()) {
                            preloadOnboarding4()
                        }
                    }
                    2 -> {
                        YNMAirBridgeDefaultEvent.pushEventScreenView(YNMAirBridge.AppData("Ob3", ""))
                        if (!preload0b4 && isFiveObd()) {
                            preloadOnboarding4()
                        }
                    }
                    3 -> {
                        YNMAirBridgeDefaultEvent.pushEventScreenView(YNMAirBridge.AppData("Ob4", ""))
                        if (isFiveObd() && !preloadOb5) {
                            preloadOnboarding5()
                        }
                        if (isFiveObd() && !preloadOb6 && RemoteConfigManager.instance!!.preloadInterFinishObdIndex.toInt() == 3) {
                            preloadOnboarding6();
                        }
                    }
                    4 -> {
                        YNMAirBridgeDefaultEvent.pushEventScreenView(YNMAirBridge.AppData("Ob5", ""))
                        if (isFiveObd() && !preloadOb6 && RemoteConfigManager.instance!!.preloadInterFinishObdIndex.toInt() == 4) {
                            preloadOnboarding6();
                        }
                    }
                }
            }
        })

        viewBinding.tvNext.setOnClickListener {
            elementClickEvent("btn_next_1", "button")
            onClickNext()
        }
        viewBinding.tvNext2.setOnClickListener {
            elementClickEvent("btn_next_2", "button")
            onClickNext()
        }
        viewBinding.btnContinue.setOnClickListener {
            elementClickEvent("btn_next_3", "button")
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
        if (isFiveObd()) {
            AdsInterMultiPreload.showPreloadedInterAdWithLoading(
                this@OnboardingActivity,
                ObdConstants.INTER_ONBOARD_6,
                10000,
                object : YNMAdsCallbacks(YNMAirBridge.AppData(nameView, ObdConstants.INTER_ONBOARD_6), YNMAds.INTERSTITIAL) {
                    override fun onNextAction(isShown: Boolean) {
                        super.onNextAction(isShown)
                        goHome()
                    }

                    override fun onAdFailedToLoad(adError: AdsError?) {
                        super.onAdFailedToLoad(adError)
                        goHome()
                    }
                }
            )
            return
        }
        goHome()
    }
    private fun goHome() {
        YNMAds.getInstance().adConfig.setInterFlow(RemoteConfigManager.instance!!.getStartIndexInter(), RemoteConfigManager.instance!!.getDeltaIndexInter())
        YNMAds.isGoHome = true;
        val bundle = Bundle().apply {
            putString(AppConstants.OPEN_FROM_SCREEN_KEY, nameView)
        }
        AppExtension.goHomeActivity(this, bundle)
        finish()
    }

    public fun getListPosNativeFull() : List<Int> {
        if (isFiveObd()) {
            return listOf(1,3);
        }
        return listOf(2)
    }
    public fun getListPosNativeNormal() : List<Int> {
        if (isFiveObd()) {
            return listOf(0,4);
        }
        return listOf(0,3)
    }
    public fun getListNoNative() : List<Int> {
        if (isFiveObd()) {
            return listOf(2)
        }
        return listOf(1)
    }
    public fun getLastPos(): Int {
        if (isFiveObd()) {
            return 4
        }
        return 3
    }

    fun showIndicatorView(pos: Int){
        viewBinding.tvNext2.visibility = View.GONE
        viewBinding.tvNext.setTextColor(if (pos == getLastPos())
            ContextCompat.getColor(this, R.color.obd_theme_color)
            else ContextCompat.getColor(this, com.ads.nomyek_admob.R.color.textColor))
        viewBinding.tvNext.text = (if (pos == getLastPos()) resources.getString(R.string.start) else resources.getString(R.string.next))
        if (pos in getListPosNativeNormal()) {
            viewBinding.indicatorView.visibility = View.VISIBLE
            viewBinding.tvNext.visibility = View.VISIBLE
            viewBinding.indicatorView2.visibility = View.INVISIBLE
            viewBinding.layoutContinue2.visibility = View.INVISIBLE
        }
        if (pos in getListNoNative()) {
            viewBinding.indicatorView.visibility = View.INVISIBLE
            viewBinding.tvNext.visibility = View.INVISIBLE
            viewBinding.indicatorView2.visibility = View.VISIBLE
            viewBinding.layoutContinue2.visibility = View.VISIBLE

        }
        if (pos in getListPosNativeFull()) {
            viewBinding.indicatorView.visibility = View.INVISIBLE
            viewBinding.tvNext.visibility = View.INVISIBLE
            viewBinding.indicatorView2.visibility = View.INVISIBLE
            viewBinding.layoutContinue2.visibility = View.INVISIBLE
            viewBinding.tvNext2.visibility = View.VISIBLE
        }
    }


}
