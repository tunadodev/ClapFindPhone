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
import com.ibl.tool.clapfindphone.onboard_flow.language.Language2Activity
import com.ibl.tool.clapfindphone.utils.purchase.PurchaseManagerInApp


class OnboardingFragmentNew(
    private var position: Int = 0,
    var idImage: Int = R.drawable.obd1,
    var idText: Int = 0,
    var idTextDetail: Int = 0
) : BaseObdFragment<FragmentObdSlideBinding>(FragmentObdSlideBinding::inflate) {
    private var isFirstPause = true
    lateinit var activity : OnboardingActivityNew;
    private var autoNextJob: Job? = null
    companion object {
        var clickNative: Boolean = false
    }
    
    private fun safeGetString(resId: Int): String? {
        return try {
            if (resId != 0) getString(resId) else null
        } catch (e: Exception) {
            null
        }
    }
    
    override fun initView() {
        activity = requireActivity() as OnboardingActivityNew
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
                            object : YNMAdsCallbacks() {
                                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                                    super.onNativeAdLoaded(nativeAd)
                                }
                                override fun onAdClicked() {
                                    super.onAdClicked()
                                    clickNative = true;
                                }
                            },
                            null
                        )
                    }
                } else {
                    binding?.nativeOnboarding?.visibility = View.GONE
                }
                Log.d("nativeOB", "ob2NativeHigh")
            }
        }
    }

    override fun addEvent() {
    }


    override fun onResume() {
        super.onResume()
        if (clickNative) {
            reShowNativeOnboarding(position)
            clickNative = false;
            activity.highlighNext()
        }
    }
    private fun reShowNativeOnboarding(position: Int) {
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
        var listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf()
        var name = ObdConstants.NATIVE_ONBOARD_1
        when (position) {
            0-> {
                listAdId = listAdId1
                name = ObdConstants.NATIVE_ONBOARD_1
            }

        }

        if (listAdId.isEmpty()) {
            return
        }

        activity?.let {
            AdsNativeMultiPreload.preloadMultipleNativeAds(
                it,
                YNMAirBridge.AppData("obd", name),
                listAdId,
                name,
                object : YNMAdsCallbacks() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                        clickNative = true;
                    }
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        binding?.nativeOnboarding?.let { adView ->
                            activity?.let { it1 ->
                                AdsNativeMultiPreload.showPreloadedNativeAd(
                                    it1,
                                    adView,
                                    name,
                                    R.layout.custom_native_admob_large,
                                    R.layout.custom_native_admob_large
                                )
                            }
                        }
                    }
                }
            )
        }
    }



    override fun onPause() {
        super.onPause()
        if (isFirstPause) {
            isFirstPause = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
