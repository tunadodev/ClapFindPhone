package com.ibl.tool.clapfindphone.onboard_flow.language

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.ads.nomyek_admob.admobs.AppOpenManager
import com.ads.nomyek_admob.event.YNMAirBridge
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.google.android.material.card.MaterialCardView
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.databinding.ActivityLanguagesBinding
import com.ibl.tool.clapfindphone.onboard_flow.onboarding.OnboardingActivity
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.SharedPref
import com.ibl.tool.clapfindphone.utils.Utils
import com.ibl.tool.clapfindphone.utils.firebaseAnalyticsEvent
import com.ibl.tool.clapfindphone.utils.purchase.PurchaseManagerInApp
import com.ads.nomyek_admob.ads_components.YNMAdsCallbacks
import com.ads.nomyek_admob.ads_components.wrappers.AdsError
import com.ads.nomyek_admob.max.AppLovinCallback
import com.ads.nomyek_admob.max.MaxNativePreload
import com.applovin.mediation.MaxError
import com.google.android.gms.ads.nativead.NativeAd
import com.ibl.tool.clapfindphone.app.ObdConstants
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager

class LanguageActivity : BaseObdActivity<ActivityLanguagesBinding>() {
    private lateinit var languageData: List<LanguageModel>
    private lateinit var btnSave: ImageView
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var listView: ListView
    private var lastChosenItem: View? = null
    private var savedLangCode: String = ""
    private var firstOpen = true
    private var isLoaded202_2 = false;
    
    override fun getLayoutActivity(): Int {
        return R.layout.activity_languages
    }
    
    override fun initViews() {
        nameView = "L1"
        initDefine()
        initAction()
        if (!Utils.isFinishObd()) preloadL2NativeAds();
    }
    
    private fun initDefine() {
        languageData = LanguageModel.getAllLangData()
        listView = viewBinding.listview
        savedLangCode = if (!Utils.isFinishObd()) "" else SharedPref.readString(AppConstants.LANG_CODE_STORE, "")
        languageAdapter = LanguageAdapter(this@LanguageActivity, languageData, savedLangCode)
        btnSave = viewBinding.btnConfirmChange
    }
    
    private fun goToNextActivity() {
        // Restart all activities in the stack
        if (!Utils.isFinishObd()) {
            AppExtension.showActivity(this, OnboardingActivity::class.java, null, 0, false)
        } else {
            AppExtension.goHomeActivity(this, null)
        }
        finishAffinity()
    }
    
    private fun initAction() {
        firebaseAnalyticsEvent(this, "view_language", "view", "view")
        viewBinding.backBtn.setOnClickListener {
            elementClickEvent("back_btn", "button")
            onBackPressedDispatcher.onBackPressed()
        }
        
        if (!Utils.isFinishObd()) {
            viewBinding.backBtn.visibility = View.GONE
        }
        
        if (!PurchaseManagerInApp.getInstance().isPurchased) {
            AdsNativeMultiPreload.showPreloadedNativeAd(
                this,
                viewBinding.nativeAd,
                ObdConstants.NATIVE_LANGUAGE1,
                R.layout.custom_native_admob_large,
                R.layout.custom_native_admob_large,
                null,
                null
            )

        } else {
            viewBinding.nativeAd.visibility = View.GONE
        }

        btnSave.visibility = if (savedLangCode == "") View.GONE else View.VISIBLE
        
        btnSave.setOnClickListener {
            elementClickEvent("btn_save", "button")
            goToNextActivity()
            SharedPref.saveString(AppConstants.LANG_CODE_STORE, savedLangCode)
        }
        
        listView.adapter = languageAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            lastChosenItem?.let {
                languageAdapter.setDeActiveButton(it)
            } ?: run {
                languageAdapter.setDeActiveButton(languageAdapter.savedLangItem)
            }
            
            val selectedLang = (view.findViewById<TextView>(R.id.lang_code)).text.toString()
            savedLangCode = selectedLang
            languageAdapter.setActiveButton(view)
            lastChosenItem = view
            languageAdapter.setSavedLang(selectedLang)
            SharedPref.saveString(AppConstants.LANG_CODE_STORE, savedLangCode)
            val intent = Intent(this, Language2Activity::class.java)
            val bundle = Bundle().apply {
                putString(AppConstants.OPEN_FROM_SCREEN_KEY, nameView)
            }
            bundle.putInt("scrollPos", listView.firstVisiblePosition)
            bundle.putBoolean("isLoaded202_2", isLoaded202_2)
            intent.putExtras(bundle)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    private fun preloadL2NativeAds() {
        var preloaded = MaxNativePreload.getInstance().getAdStatus(ObdConstants.PRELOAD_NATIVE_202_1) == MaxNativePreload.AdStatus.LOADED || AdsNativeMultiPreload.getPreloadState(ObdConstants.PRELOAD_NATIVE_202_1) == AdsNativeMultiPreload.PreloadState.LOADED;
        if (RemoteConfigManager.instance!!.getListAdIsOrder202().isNotEmpty() && !preloaded) {
            if (RemoteConfigManager.instance!!.getListAdIsOrder202()[1].equals("Max")) {
                MaxNativePreload.getInstance().preloadNative(
                    this,
                    RemoteConfigManager.instance!!.getLfo2MaxAdId(),
                    ObdConstants.PRELOAD_NATIVE_202_2,
                    R.layout.custom_native_admob_large,
                    object : AppLovinCallback() {
                        override fun onAdClicked() {
                            super.onAdClicked()
                            Language2Activity.clickNative = true;
                        }

                        override fun onAdFailedToLoad(i: MaxError?) {
                            super.onAdFailedToLoad(i)
                            isLoaded202_2 = true;
                            // Notify Language2Activity that ads preload is complete (preloading = false)
                            Language2Activity.adPreloadIsLoading.postValue(false)
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            isLoaded202_2 = true;
                            // Notify Language2Activity that ads preload is complete (preloading = false)
                            Language2Activity.adPreloadIsLoading.postValue(false)
                        }
                    })
            }
            else {
                var listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                    AdsNativeMultiPreload.AdIdModel().apply {
                        adId = RemoteConfigManager.instance!!.getLfo2HighAdId()
                        adName = "native_language_2_high"
                    }
                )
                if (RemoteConfigManager.instance!!.getListAdIsOrder202()[1].equals("All")) {
                    listAdId = listOf(
                        AdsNativeMultiPreload.AdIdModel().apply {
                            adId = RemoteConfigManager.instance!!.getLfo2AdId()
                            adName = "native_language_2"
                        }
                    )
                }

                AdsNativeMultiPreload.preloadMultipleNativeAds(
                    this@LanguageActivity,
                    YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_LANGUAGE2),
                    listAdId,
                    ObdConstants.PRELOAD_NATIVE_202_2,
                    object : YNMAdsCallbacks() {
                        override fun onAdClicked() {
                            super.onAdClicked()
                            Language2Activity.clickNative = true;
                        }

                        override fun onNativeAdLoaded(nativeAd: NativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            isLoaded202_2 = true;
                            // Notify Language2Activity that ads preload is complete (preloading = false)
                            Language2Activity.adPreloadIsLoading.postValue(false)
                        }

                        override fun onAdFailedToLoad(adError: AdsError?) {
                            super.onAdFailedToLoad(adError)
                            isLoaded202_2 = true;
                            // Notify Language2Activity that ads preload is complete (preloading = false)
                            Language2Activity.adPreloadIsLoading.postValue(false)
                        }
                    }
                )
            }
        }
    }


    private fun reShowNativeLanguageAds() {
        val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getLfo1HighAdId()
                adName = "native_language_1_high"
            },
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getLfo1AdId()
                adName = "native_language_1"
            }
        )

        AdsNativeMultiPreload.preloadMultipleNativeAds(
            this@LanguageActivity,
            YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_LANGUAGE1),
            listAdId,
            ObdConstants.NATIVE_LANGUAGE1,
            object : YNMAdsCallbacks() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    // Show the native ad in the native ad view if available
                    viewBinding?.nativeAd?.let { adView ->
                        AdsNativeMultiPreload.showPreloadedNativeAd(
                            this@LanguageActivity,
                            adView,
                            ObdConstants.NATIVE_LANGUAGE1,
                            R.layout.custom_native_admob_large,
                            R.layout.custom_native_admob_large
                        )
                    }
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (!AppOpenManager.getInstance().isInterstitialShowing()) {
            if (!firstOpen) reShowNativeLanguageAds();
            firstOpen = false
        }
    }
}
