package com.ibl.tool.clapfindphone.onboard_flow.onboarding

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import com.ads.nomyek_admob.ads_components.YNMAdsCallbacks
import com.ads.nomyek_admob.ads_components.wrappers.AdsError
import com.ads.nomyek_admob.event.YNMAirBridge
import com.ads.nomyek_admob.max.MaxNativePreload
import com.ads.nomyek_admob.utils.AdsInterPreload
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.bumptech.glide.Glide
import com.google.android.gms.ads.nativead.NativeAd
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.app.ObdConstants
import com.ibl.tool.clapfindphone.databinding.FragmentObdSlideBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdFragment
import com.ibl.tool.clapfindphone.utils.purchase.PurchaseManagerInApp


class OnboardingFragment(
    private var position: Int = 0,
    var idImage: Int = R.drawable.obd1,
    var idText: Int = 0,
    var idTextDetail: Int = 0
) : BaseObdFragment<FragmentObdSlideBinding>(FragmentObdSlideBinding::inflate) {
    private var isFirstPause = true
    companion object {
        var isClickNativeFull = false
    }
    private var showMax = false;
    lateinit var activity : OnboardingActivity;
    private var autoNextJob: Job? = null
    
    private fun safeGetString(resId: Int): String? {
        return try {
            if (resId != 0) getString(resId) else null
        } catch (e: Exception) {
            null
        }
    }
    
    override fun initView() {
        activity = requireActivity() as OnboardingActivity
        binding?.imgInside?.setImageResource(idImage)
        
        // Safe string resource handling
        safeGetString(idText)?.let { binding?.tvInside?.text = it }
        safeGetString(idTextDetail)?.let { binding?.tvDetail?.text = it }

        when (position) {
            0 -> {
                if (!PurchaseManagerInApp.getInstance().isPurchased) {
                    context?.let {
                        AdsNativeMultiPreload.showPreloadedNativeAd(
                            it,
                            binding!!.nativeOnboarding,
                            ObdConstants.NATIVE_ONBOARD_1,
                            R.layout.custom_native_admob_large,
                            R.layout.custom_native_admob_large,
                            null,
                            null
                        )
                    }
                } else {
                    binding?.nativeOnboarding?.visibility = View.GONE
                }
                Log.d("nativeOB", "ob2NativeHigh")
            }

            1 -> {
                if (activity.isFiveObd()) {
                    binding?.nativeOnboarding?.visibility = View.GONE
                    binding?.nativeOnboardingFull?.visibility = View.VISIBLE
                    setupLoadingOb3Observer();
                } else {
                    binding?.clFrad?.visibility = View.VISIBLE
                    binding?.lottie?.setVisibility(View.VISIBLE)
                    binding?.nativeOnboarding?.visibility = View.GONE
                }
            }

            2 -> {
                if (activity.isFiveObd()) {
                    binding?.clFrad?.visibility = View.VISIBLE
                    binding?.lottie?.setVisibility(View.VISIBLE)
                    binding?.nativeOnboarding?.visibility = View.GONE
                } else {
                    binding?.nativeOnboarding?.visibility = View.GONE
                    binding?.nativeOnboardingFull?.visibility = View.VISIBLE
                    setupLoadingOb3Observer()
                }
            }

            3 -> {
                if (activity.isFiveObd()) {
                    binding?.nativeOnboarding?.visibility = View.GONE
                    binding?.nativeOnboardingFull?.visibility = View.VISIBLE
                    context?.let {
                        AdsNativeMultiPreload.showPreloadedNativeAd(
                            it,
                            binding!!.nativeOnboardingFull,
                            ObdConstants.NATIVE_ONBOARD_4,
                            R.layout.custom_full_screen_native_ads,
                            R.layout.custom_full_screen_native_ads,
                            object : YNMAdsCallbacks() {
                                override fun onAdFailedToLoad(adError: AdsError?) {
                                    super.onAdFailedToLoad(adError)
                                    if (isAdded && context != null) {
                                        binding?.nativeOnboardingFull?.visibility = View.GONE
                                        showDefaultScreen()
                                    }
                                }

                                override fun onAdClicked() {
                                    super.onAdClicked()
                                    isClickNativeFull = true;
                                }
                            },
                            null
                        )
                    }
                } else {
                    context?.let {
                        AdsNativeMultiPreload.showPreloadedNativeAd(
                            it,
                            binding!!.nativeOnboarding,
                            ObdConstants.NATIVE_ONBOARD_4,
                            R.layout.custom_native_admob_large,
                            R.layout.custom_native_admob_large,
                            null,
                            null
                        )
                    }
                }
            }
            4 -> {
                if (activity.isFiveObd()) {
                    context?.let {
                        AdsNativeMultiPreload.showPreloadedNativeAd(
                            it,
                            binding!!.nativeOnboarding,
                            ObdConstants.NATIVE_ONBOARD_5,
                            R.layout.custom_native_admob_large,
                            R.layout.custom_native_admob_large,
                            null,
                            null
                        )
                    }
                }
            }
        }
    }

    override fun addEvent() {
    }
    
    private fun setupLoadingOb3Observer() {
        if (!isAdded || activity == null) {
            return
        }
        val activity = activity as? OnboardingActivity ?: return
        activity.loadingOb2LiveData.observe(this, Observer { isLoading ->
            if (!isLoading && isAdded && context != null) {
                // When loadingOb3 becomes false, show native ad
                showNativeObd3()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (position in activity.getListPosNativeFull()) {
            if (isClickNativeFull) {
                isClickNativeFull = false;
                if (isAdded && activity != null) {
                    (activity as? OnboardingActivity)?.onClickNext()
                }
            } else {
                // Start auto next job after 7 seconds
                startAutoNextJob()
            }
            return
        }
        reShowNativeOnboarding(position)
    }

    private fun showNativeObd3() {
        var maxId = "";
        if (MaxNativePreload.getInstance().getAdStatus(ObdConstants.PRELOAD_NATIVE_303_1) == MaxNativePreload.AdStatus.LOADED) {
            maxId = ObdConstants.PRELOAD_NATIVE_303_1;
        }
        if (MaxNativePreload.getInstance().getAdStatus(ObdConstants.PRELOAD_NATIVE_303_2) == MaxNativePreload.AdStatus.LOADED) {
            maxId = ObdConstants.PRELOAD_NATIVE_303_2;
        }
        if (!maxId.isEmpty()) {
            showMax = true;
            MaxNativePreload.getInstance().showNative(maxId, binding!!.nativeOnboardingFull)
            return
        }
        var admobId = "";
        if (AdsNativeMultiPreload.getPreloadState(ObdConstants.PRELOAD_NATIVE_303_1) == AdsNativeMultiPreload.PreloadState.LOADED) {
            admobId = ObdConstants.PRELOAD_NATIVE_303_1;
        }
        if (AdsNativeMultiPreload.getPreloadState(ObdConstants.PRELOAD_NATIVE_303_2) == AdsNativeMultiPreload.PreloadState.LOADED) {
            admobId = ObdConstants.PRELOAD_NATIVE_303_2;
        }
        if (AdsNativeMultiPreload.getPreloadState(ObdConstants.PRELOAD_NATIVE_303_3) == AdsNativeMultiPreload.PreloadState.LOADED) {
            admobId = ObdConstants.PRELOAD_NATIVE_303_3;
        }
        if (!admobId.isEmpty()) {
            context?.let {
                AdsNativeMultiPreload.showPreloadedNativeAd(
                    it,
                    binding!!.nativeOnboardingFull,
                    admobId,
                    R.layout.custom_full_screen_native_ads,
                    R.layout.custom_full_screen_native_ads,
                    object : YNMAdsCallbacks() {
                        override fun onAdClicked() {
                            super.onAdClicked()
                            isClickNativeFull = true;
                        }
                        override fun onAdFailedToLoad(adError: AdsError?) {
                            super.onAdFailedToLoad(adError)
                            if (isAdded && context != null) {
                                binding?.nativeOnboardingFull?.visibility = View.GONE
                                showDefaultScreen()
                            }
                        }
                    },
                    null
                )
            }
        } else {
            binding?.nativeOnboardingFull?.visibility = View.GONE
            showDefaultScreen()
        }
    }

    private fun reShowNativeOnboarding(position: Int) {
        if (!isAdded || activity == null) {
            return
        }
        
        val onboardingActivity = activity as? OnboardingActivity ?: return
        
        if (position == onboardingActivity.getListPosNativeFull().first() && showMax) {
            onboardingActivity.loadingOb2LiveData.postValue(true)
            onboardingActivity.preloadOnboarding2()
            return;
        }
        val listAdId1: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb1HighAdId()
                adName = "native_onboard_1_high"
            },
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb1AdId()
                adName = "native_onboard_1"
            }
        )

        val listAdId3: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb2HighAdId()
                adName = "native_onboard_2_high"
            },
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb2AdId()
                adName = "native_onboard_2"
            }
        )

        val listAdId4: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb4HighAdId()
                adName = "native_onboard_4_high"
            },
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb4AdId()
                adName = "native_onboard_4"
            }
        )

        val listAdId5: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb5HighAdId()
                adName = "native_onboard_5_high"
            },
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getOb5AdId()
                adName = "native_onboard_5"
            }
        )
        var listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf()
        var name = ObdConstants.NATIVE_ONBOARD_1
        when (position) {
            0-> {
                listAdId = listAdId1
                name = ObdConstants.NATIVE_ONBOARD_1
            }
            1-> {
                if (onboardingActivity.isFiveObd()) {
                    listAdId = listAdId3
                    name = ObdConstants.NATIVE_ONBOARD_2
                }
            }
            2 -> {
                if (!onboardingActivity.isFiveObd()) {
                    listAdId = listAdId3
                    name = ObdConstants.NATIVE_ONBOARD_2
                }
            }
            3 -> {
                listAdId = listAdId4
                name = ObdConstants.NATIVE_ONBOARD_4
            }
            4 -> {
                if (onboardingActivity.isFiveObd()) {
                    listAdId = listAdId5
                    name = ObdConstants.NATIVE_ONBOARD_5
                }
            }
        }

        if (listAdId.isEmpty()) {
            return
        }

        onboardingActivity.let {
            AdsNativeMultiPreload.preloadMultipleNativeAds(
                it,
                YNMAirBridge.AppData("obd", name),
                listAdId,
                name,
                object : YNMAdsCallbacks() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                        isClickNativeFull = true;
                    }
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        // Check if fragment is still attached
                        if (!isAdded || context == null || activity == null) {
                            return
                        }
                        
                        val currentActivity = activity as? OnboardingActivity ?: return
                        
                        // Show the native ad in the native ad view if available
                        if (position in currentActivity.getListPosNativeFull()) {
                            binding?.nativeOnboardingFull?.visibility = View.VISIBLE
                            binding?.nativeOnboardingFull?.let { adView ->
                                AdsNativeMultiPreload.showPreloadedNativeAd(
                                    currentActivity,
                                    adView,
                                    name,
                                    R.layout.custom_full_screen_native_ads,
                                    R.layout.custom_full_screen_native_ads
                                )
                            }
                            return;
                        }
                        binding?.nativeOnboarding?.let { adView ->
                            AdsNativeMultiPreload.showPreloadedNativeAd(
                                currentActivity,
                                adView,
                                name,
                                R.layout.custom_native_admob_large,
                                R.layout.custom_native_admob_large
                            )
                        }
                    }
                }
            )
        }
    }

    private fun showDefaultScreen() {
        // Check if fragment is still attached to activity
        if (!isAdded || context == null) {
            return
        }
        
        binding?.llMain?.visibility = View.VISIBLE
        binding?.imgInside?.visibility = View.VISIBLE
        binding?.imgInside?.setImageResource(idImage)
        
        // Safe string resource handling
        safeGetString(idText)?.let { binding?.tvInside?.text = it }

        binding?.let {
            context?.let { ctx ->
                Glide.with(ctx).load(idImage)
                    .into(it.imgInside)
            }
        }
        
        binding?.nativeOnboarding?.visibility = View.GONE
        binding?.nativeOnboardingFull?.visibility = View.GONE
        
        // Safe activity access
        if (isAdded && activity != null) {
            (activity as? OnboardingActivity)?.showIndicatorView(0)
        }
    }

    private fun startAutoNextJob() {
        // Cancel any existing job first
        cancelAutoNextJob()
        
        autoNextJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                delay(7000) // Wait 7 seconds
                if (isAdded && !isDetached && isResumed && activity != null) {
                    (activity as? OnboardingActivity)?.onClickNext()
                }
            } catch (e: CancellationException) {
                // Job was cancelled, do nothing
                Log.d("OnboardingFragment", "Auto next job cancelled")
            }
        }
    }

    private fun cancelAutoNextJob() {
        if (autoNextJob != null) {
            autoNextJob?.cancel()
            autoNextJob = null
        }
    }


    override fun onPause() {
        super.onPause()
        if (isFirstPause) {
            isFirstPause = false
        }
        // Cancel auto next job when app goes to background
        cancelAutoNextJob()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel auto next job when fragment is destroyed
        cancelAutoNextJob()
    }

}
