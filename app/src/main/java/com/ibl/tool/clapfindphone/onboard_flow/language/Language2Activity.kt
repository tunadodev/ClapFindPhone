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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ads.nomyek_admob.admobs.AppOpenManager
import com.ads.nomyek_admob.ads_components.YNMAdsCallbacks
import com.ads.nomyek_admob.event.YNMAirBridge
import com.ads.nomyek_admob.max.MaxNativePreload
import com.ads.nomyek_admob.max.MaxNew
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.material.card.MaterialCardView
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.app.ObdConstants
import com.ibl.tool.clapfindphone.databinding.ActivityLanguagesBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.onboard_flow.onboarding.OnboardingActivity
import com.ibl.tool.clapfindphone.onboard_flow.onboarding.OnboardingActivityNew
import com.ibl.tool.clapfindphone.onboard_flow.onboarding.OnboardingFragmentNew
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.SharedPref
import com.ibl.tool.clapfindphone.utils.Utils
import com.ibl.tool.clapfindphone.utils.firebaseAnalyticsEvent
import com.ibl.tool.clapfindphone.utils.purchase.PurchaseManagerInApp
import kotlin.math.PI

class Language2Activity : BaseObdActivity<ActivityLanguagesBinding>() {
    
    private lateinit var languageData: List<LanguageModel>
    private lateinit var btnSave: ImageView
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var listView: ListView
    private var lastChosenItem: View? = null
    private var savedLangCode: String = ""
    private var position: Int = 0
    private var index: Long = 0
    private var firstOpen = true
    private var isAdsLoadedFromL1: Boolean = false
    private var loadingHandler: Handler? = null
    private var isLoadingComplete = false
    private var isAdsShown = false
    private var adsObserver: Observer<Boolean>? = null
    
    companion object {
        var clickNative = false
        // Shared LiveData for ads preload status communication between activities
        // true = still preloading, false = preload completed (success or failed)
        val adPreloadIsLoading = MutableLiveData<Boolean>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
    }
    
    override fun getLayoutActivity(): Int {
        return R.layout.activity_languages
    }
    
    override fun initViews() {
        nameView = "L2"
        // Get ads loaded status from previous activity first
        val bundle = intent.extras
        isAdsLoadedFromL1 = bundle?.getBoolean("isLoaded202_2", false) ?: false
        
        // Reset LiveData for fresh observation (true = still preloading)
        // Only set to true if ads were not already loaded from L1
        adPreloadIsLoading.value = !isAdsLoadedFromL1
        initDefine()
        initAction()
        setupAdsObserver()
        preloadOnboarding1()
    }
    
    private fun initDefine() {
        languageData = LanguageModel.getAllLangData()
        listView = viewBinding.listview
        savedLangCode = SharedPref.readString(AppConstants.LANG_CODE_STORE, "")
        languageAdapter = LanguageAdapter(this@Language2Activity, languageData, savedLangCode)
        btnSave = viewBinding.btnConfirmChange
        
        viewBinding.loadingAnim.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
        
        // Dynamic timing based on ads loading status
        val loadingDuration = if (isAdsLoadedFromL1) 1500L else 2500L
        
        // Start loading animation timer (independent of ads)
        loadingHandler = Handler(Looper.getMainLooper())
        loadingHandler?.postDelayed({
            completeLoadingAnimation()
        }, loadingDuration)
        
        // If ads were already loaded from L1, show them immediately
        if (isAdsLoadedFromL1 && !PurchaseManagerInApp.getInstance().isPurchased) {
            showAdWhenReady()
        }
    }
    
    private fun setupAdsObserver() {
        // Only observe if ads were not already loaded from L1
        if (!isAdsLoadedFromL1) {
            adsObserver = Observer { isStillPreloading ->
                if (!isStillPreloading && !isAdsShown) {
                    // Ads preload completed - show ads immediately (independent of animation)
                    showAdWhenReady()
                }
            }
            adPreloadIsLoading.observe(this, adsObserver!!)
        }
    }
    
    private fun completeLoadingAnimation() {
        if (isLoadingComplete) return
        isLoadingComplete = true
        
        viewBinding.loadingAnim.visibility = View.GONE
        btnSave.visibility = View.VISIBLE
        
        // Show ads after loading animation completes (if not already shown)
        if (!PurchaseManagerInApp.getInstance().isPurchased && !isAdsShown) {
            showAdWhenReady()
        }
    }
    
    private fun showAdWhenReady() {
        if (isAdsShown || PurchaseManagerInApp.getInstance().isPurchased) return
        isAdsShown = true
        showAd()
    }
    
    private fun goToNextActivity() {
        // Restart all activities in the stack
        val bundle = Bundle().apply {
            putString(AppConstants.OPEN_FROM_SCREEN_KEY, nameView)
        }
        if (!Utils.isFinishObd()) {
            if (ObdConstants.TEST_1_ONBOARDING) {
                AppExtension.showActivity(this, OnboardingActivityNew::class.java, bundle, 0, false)
                return
            }
            AppExtension.showActivity(this, OnboardingActivity::class.java, bundle, 0, false)
        } else {
            AppExtension.goHomeActivity(this, bundle)
        }
        finishAffinity()
    }

    private fun showAd() {
        if (MaxNativePreload.getInstance().getAdStatus(ObdConstants.PRELOAD_NATIVE_202_1) == MaxNativePreload.AdStatus.LOADED) {
            MaxNativePreload.getInstance().showNative(
                ObdConstants.PRELOAD_NATIVE_202_1,
                viewBinding.nativeAd,
                null)
            return;
        }

        if (MaxNativePreload.getInstance().getAdStatus(ObdConstants.PRELOAD_NATIVE_202_2) == MaxNativePreload.AdStatus.LOADED) {
            MaxNativePreload.getInstance().showNative(
                ObdConstants.PRELOAD_NATIVE_202_2,
                viewBinding.nativeAd,
                null)
            return;
        }

        if (AdsNativeMultiPreload.getPreloadState(ObdConstants.PRELOAD_NATIVE_202_1) == AdsNativeMultiPreload.PreloadState.LOADED) {
            AdsNativeMultiPreload.showPreloadedNativeAd(
                this,
                viewBinding.nativeAd,
                ObdConstants.PRELOAD_NATIVE_202_1,
                R.layout.custom_native_admob_large,
                R.layout.custom_native_admob_large,
                null,null
            )
            return;
        }

        if (AdsNativeMultiPreload.getPreloadState(ObdConstants.PRELOAD_NATIVE_202_2) == AdsNativeMultiPreload.PreloadState.LOADED) {
            AdsNativeMultiPreload.showPreloadedNativeAd(
                this,
                viewBinding.nativeAd,
                ObdConstants.PRELOAD_NATIVE_202_2,
                R.layout.custom_native_admob_large,
                R.layout.custom_native_admob_large,
                null,null
            )
            return;
        }

        if (RemoteConfigManager.instance!!.getListAdIsOrder202()[2].equals("Max")) {
            MaxNew.getInstance().loadNativeAdNew(
                this,
                viewBinding.nativeAd,
                RemoteConfigManager.instance!!.getLfo2MaxAdId(),
                R.layout.custom_native_admob_large
            )
            return
        }
        if (ObdConstants.TEST_1_ONBOARDING) {
            var listAdId = listOf(
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getLfo2AdId()
                    adName = "native_language_2"
                }
            )
            AdsNativeMultiPreload.preloadMultipleNativeAds(
                this@Language2Activity,
                YNMAirBridge.AppData(nameView, ObdConstants.PRELOAD_NATIVE_202_3),
                listAdId,
                ObdConstants.PRELOAD_NATIVE_202_3,
                object : YNMAdsCallbacks() {
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        viewBinding?.nativeAd?.let { adView ->
                            AdsNativeMultiPreload.showPreloadedNativeAd(
                                this@Language2Activity,
                                adView,
                                ObdConstants.PRELOAD_NATIVE_202_3,
                                R.layout.custom_native_admob_large,
                                R.layout.custom_native_admob_large
                            )
                        }
                    }
                    override fun onAdClicked() {
                        super.onAdClicked()
                        Language2Activity.clickNative = true;
                    }
                }
            )
        } else {
            var listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getLfo2HighAdId()
                    adName = "native_language_2_high"
                }
            )
            if (RemoteConfigManager.instance!!.getListAdIsOrder202()[2].equals("All")) {
                listAdId = listOf(
                    AdsNativeMultiPreload.AdIdModel().apply {
                        adId = RemoteConfigManager.instance!!.getLfo2AdId()
                        adName = "native_language_2"
                    }
                )
            }

            AdsNativeMultiPreload.preloadMultipleNativeAds(
                this@Language2Activity,
                YNMAirBridge.AppData(nameView, ObdConstants.PRELOAD_NATIVE_202_3),
                listAdId,
                ObdConstants.PRELOAD_NATIVE_202_3,
                object : YNMAdsCallbacks() {
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        viewBinding?.nativeAd?.let { adView ->
                            AdsNativeMultiPreload.showPreloadedNativeAd(
                                this@Language2Activity,
                                adView,
                                ObdConstants.PRELOAD_NATIVE_202_3,
                                R.layout.custom_native_admob_large,
                                R.layout.custom_native_admob_large
                            )
                        }
                    }
                    override fun onAdClicked() {
                        super.onAdClicked()
                        Language2Activity.clickNative = true;
                    }
                }
            )
        }
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
        
        // Hide native ad view if user has purchased premium
        if (PurchaseManagerInApp.getInstance().isPurchased) {
            viewBinding.nativeAd.visibility = View.GONE
        }
        
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
            
            // set texts:
            viewBinding.txtTitle.text = AppExtension.getLocalizedText(this, selectedLang, R.string.language)
        }
        
        // Restore scroll position from previous activity
        val bundle = intent.extras
        bundle?.let {
            listView.setSelection(it.getInt("scrollPos", 0))
        }
    }

    private fun reShowNativeLanguageAds() {
        val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getLfo2HighAdId()
                adName = "native_language_2_high"
            },
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getLfo2AdId()
                adName = "native_language_2"
            }
        )

        AdsNativeMultiPreload.preloadMultipleNativeAds(
            this@Language2Activity,
            YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_LANGUAGE2),
            listAdId,
            ObdConstants.NATIVE_LANGUAGE2,
            object : YNMAdsCallbacks() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    // Show the native ad in the native ad view if available
                    viewBinding?.nativeAd?.let { adView ->
                        AdsNativeMultiPreload.showPreloadedNativeAd(
                            this@Language2Activity,
                            adView,
                            ObdConstants.NATIVE_LANGUAGE2,
                            R.layout.custom_native_admob_large,
                            R.layout.custom_native_admob_large
                        )
                    }
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    clickNative = true;
                }
            }
        )
    }
    override fun onResume() {
        super.onResume()
        if (!AppOpenManager.getInstance().isInterstitialShowing()) {
            if (!firstOpen) {
                if (clickNative) {
                    goToNextActivity()
                } else {
                    reShowNativeLanguageAds()
                }
            };
            firstOpen = false
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up handlers to prevent memory leaks
        loadingHandler?.removeCallbacksAndMessages(null)
        loadingHandler = null
        
        // Remove observer from companion object LiveData to prevent memory leaks
        adsObserver?.let { observer ->
            adPreloadIsLoading.removeObserver(observer)
        }
        adsObserver = null
    }
    
    private fun preloadOnboarding1() {
        if (!Utils.isFinishObd()) {
            val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb1HighAdId()
                    adName = "native_onboard_1_high"
                },
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = RemoteConfigManager.instance!!.getOb1AdId()
                    adName = "native_onboard_1"
                }
            )

            AdsNativeMultiPreload.preloadMultipleNativeAds(
                this@Language2Activity,
                YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_ONBOARD_1),
                listAdId,
                ObdConstants.NATIVE_ONBOARD_1,
                object : YNMAdsCallbacks() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                        OnboardingFragmentNew.clickNative = true;
                    }
                }
            )
        }
    }
}
