package com.ibl.tool.clapfindphone.onboard_flow.splash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.ads.nomyek_admob.ads_components.YNMAds
import com.ads.nomyek_admob.ads_components.YNMAdsCallbacks
import com.ads.nomyek_admob.ads_components.YNMInitCallback
import com.ads.nomyek_admob.ads_components.wrappers.AdsError
import com.ads.nomyek_admob.event.YNMAirBridge
import com.ads.nomyek_admob.event.YNMAirBridgeDefaultEvent
import com.ads.nomyek_admob.max.AppLovinCallback
import com.ads.nomyek_admob.max.MaxNativePreload
import com.ads.nomyek_admob.utils.AdsInterMultiPreload
import com.ads.nomyek_admob.utils.AdsInterPreload
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.applovin.mediation.MaxError
import com.bumptech.glide.Glide
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.app.ObdConstants
import com.ibl.tool.clapfindphone.databinding.ActivitySplashScreenBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.onboard_flow.consent_dialog.ConsentDialogManager
import com.ibl.tool.clapfindphone.onboard_flow.language.Language2Activity
import com.ibl.tool.clapfindphone.onboard_flow.language.LanguageActivity
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.onboard_flow.utils.ObdUtils
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.InternetUtil
import com.ibl.tool.clapfindphone.utils.LocaleHelper
import com.ibl.tool.clapfindphone.utils.SharedPref
import com.ibl.tool.clapfindphone.utils.Utils
import com.ibl.tool.clapfindphone.utils.purchase.PurchaseManagerInApp
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseObdActivity<ActivitySplashScreenBinding>() {

    private var isStartNextActivityCalled = false
//    private val canNextScreen = MutableLiveData(false)
    private var timeOutLoading: Long = 15000
    
    // Tracking conditions for showing continue button
    private var isSplashNativeLoaded = false
    private var isSplashInterPreloaded = false
    private var isL1NativePreloaded = false
    private var isL2NativePreloaded = false
    private var isProgressCompleted = false
    private var canShowContinueButton = false
    private var isNativeSplashClicked = false;
    
    // Handler để quản lý timeout
    private val timeoutHandler = Handler(Looper.getMainLooper())
    private val timeoutRunnable = Runnable {
        YNMAirBridge.getInstance().logCustomEvent("open_splash_timeout")
        onTimeoutReached()
    }

    override fun getLayoutActivity(): Int {
        return R.layout.activity_splash_screen
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (getIntent().extras != null) {
            handleNotificationClick(getIntent())
        }
    }

    override fun initViews() {
        nameView = "Splash"
        initDefine()
        initAction()
        YNMAirBridge.getInstance().logCustomEvent(("app_open"))
        // Set default language based on device locale if first time opening
        if (!Utils.isFinishObd()) {
            setDefaultLanguageBasedOnDeviceLocale()
        }
        // Theo dõi click vào thông báo
        if (intent.extras != null) {
            handleNotificationClick(intent)
        }
        // Load GIF using Glide
        RemoteConfigManager.instance!!.loadTimeOutSplash(this, object : RemoteConfigManager.NumberCallback {
            override fun onResult(value: Long) {
                ObdUtils.checkAndPushRetentionEvents(this@SplashActivity)
                timeOutLoading = value
                // Start progress bar with callback
                viewBinding?.splashProgressBar?.start(timeOutLoading.toLong(), object : SplashProgressBar.ProgressCallback {
                    override fun onProgressCompleted() {
                        isProgressCompleted = true
                        checkAndShowContinueButton()
                    }
                })
                // Set timeout
                timeoutHandler.postDelayed(timeoutRunnable, timeOutLoading.toLong())
            }
        })
    }

    private fun initDefine() {
        Utils.setUpSplashApp()
    }

    private fun initAction() {
        Log.d("IAPNE", "IAP: ${PurchaseManagerInApp.getInstance().isPurchased()}")
        
        if (!InternetUtil.isNetworkAvailable(this)) {
            showNoNetworkDialog()
            return
        }
        
        ConsentDialogManager.instance!!.showDialogConsentMonkey(
            this,
            object : ConsentDialogManager.ConsentDialogListener {
                override fun onConsentFormDismissed(state: ConsentDialogManager.ConsentDialogState) {
                    if (state == ConsentDialogManager.ConsentDialogState.ACCEPTED) {
                        initAndShowAd()
                        return
                    }
                    startNextActivity();
                }
            }
        )
    }

    private fun initAndShowAd() {
        RemoteConfigManager.instance!!.loadTimeOutSplash(this, object : RemoteConfigManager.NumberCallback {
            override fun onResult(value: Long) {
                Thread {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    runOnUiThread {
                        // Preload native ads for splash screen
                        if (!Utils.isDisableObdAd()) {
                            YNMAds.getInstance().setInitCallback(YNMInitCallback {
//                                MobileAds.openAdInspector(this@SplashActivity) { error ->
//                                    Log.d("", "MobileAds onResult: error " +error.toString())
//                                    // Error will be non-null if ad inspector closed due to an error.
//                                }
                                if (RemoteConfigManager.instance!!.formatTypeSplash.compareTo("native") == 0) {
                                    viewBinding.nativeOnboarding.visibility = View.VISIBLE
                                    showSplashNativeAds()
                                } else {
                                    viewBinding.bannerView.visibility = View.VISIBLE
                                    setListBannerId(listOf(RemoteConfigManager.instance!!.getBannerHighSplAdId(), RemoteConfigManager.instance!!.getBannerSplAdId()))
                                    setRefreshBannerTime(RemoteConfigManager.instance!!.timeReloadBanner.toInt())
                                    showRefreshMultiIdBanner()
                                }

                                // Show interstitial ads
                                preloadSplashInterstial()

                                if (!Utils.isFinishObd()) {
                                    if (ObdConstants.TEST_1_ONBOARDING) {
                                        isL1NativePreloaded = true;
                                    } else {
                                        preloadL1NativeAds()
                                    }
                                    preloadL2NativeAds()
                                } else {
                                    isL1NativePreloaded = true
                                    isL2NativePreloaded = true
                                    checkAndShowContinueButton()
                                }
                            })
                        } else {
                            isL1NativePreloaded = true;
                            isSplashNativeLoaded = true;
                            isSplashInterPreloaded = true;
                            isL2NativePreloaded = true;
                            checkAndShowContinueButton()
                        }
                        // TODO: Request an ad.
                    }
                }.start()
            }
        })
    }

    private fun showSplashNativeAds() {
        isNativeSplashClicked = false
        val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getNativeHighSplAdId()
                adName = "native_splash_high"
                retryCount = 1
                delayTimeBeforeRetry = 300
            },
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getNativeSplAdId()
                adName = "native_splash"
            }
        )
        if (RemoteConfigManager.instance!!.retryHigh == false) {
            listAdId[0].retryCount = 0
            listAdId[0].delayTimeBeforeRetry = 0
        }

        AdsNativeMultiPreload.preloadMultipleNativeAds(
            this@SplashActivity,
            YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_SPLASH),
            listAdId,
            ObdConstants.NATIVE_SPLASH,
            object : YNMAdsCallbacks() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    isNativeSplashClicked = true;

                }
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    Log.d("SplashActivity", "Native ad loaded successfully for splash")
                    isSplashNativeLoaded = true
                    checkAndShowContinueButton()
                    
                    // Show the native ad in the native ad view if available
                    viewBinding?.nativeOnboarding?.let { adView ->
                        AdsNativeMultiPreload.showPreloadedNativeAd(
                            this@SplashActivity,
                            adView,
                            ObdConstants.NATIVE_SPLASH,
                            R.layout.custom_native_admob_large_splash,
                            R.layout.custom_native_admob_large_splash
                        )
                    }
                }
                
                override fun onAdFailedToLoad(adError: AdsError?) {
                    super.onAdFailedToLoad(adError)
                    Log.e("SplashActivity", "Failed to load splash native ad: ${adError?.message}")
                    isSplashNativeLoaded = true // Consider failed as loaded to not block UI
                    checkAndShowContinueButton()
                }
            }
        )
    }

    private fun preloadL1NativeAds() {
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
            this@SplashActivity,
            YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_LANGUAGE1),
            listAdId,
            ObdConstants.NATIVE_LANGUAGE1,
            object : YNMAdsCallbacks() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    Log.d("SplashActivity", "L1 Native ad loaded successfully")
                    isL1NativePreloaded = true
                    checkAndShowContinueButton()
                }
                
                override fun onAdFailedToLoad(adError: AdsError?) {
                    super.onAdFailedToLoad(adError)
                    Log.e("SplashActivity", "Failed to load L1 native ad: ${adError?.message}")
                    isL1NativePreloaded = true // Consider failed as loaded to not block UI
                    checkAndShowContinueButton()
                }
            }
        )
    }

    private fun preloadL2NativeAds() {
        var listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getLfo2HighAdId()
                adName = "native_language_2_high"
            }
        )

        AdsNativeMultiPreload.preloadMultipleNativeAds(
            this@SplashActivity,
            YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_LANGUAGE2),
            listAdId,
            ObdConstants.PRELOAD_NATIVE_202_1,
            object : YNMAdsCallbacks() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    Language2Activity.clickNative = true;
                }

                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    super.onNativeAdLoaded(nativeAd)
                    isL2NativePreloaded = true;
                    checkAndShowContinueButton()
                }

                override fun onAdFailedToLoad(adError: AdsError?) {
                    super.onAdFailedToLoad(adError)
                    isL2NativePreloaded = true;
                    checkAndShowContinueButton()
                }
            }
        )
//        if (RemoteConfigManager.instance!!.getListAdIsOrder202().isNotEmpty()) {
//            if (RemoteConfigManager.instance!!.getListAdIsOrder202()[0].equals("Max")) {
//                MaxNativePreload.getInstance().preloadNative(
//                    this,
//                    RemoteConfigManager.instance!!.getLfo2MaxAdId(),
//                    ObdConstants.PRELOAD_NATIVE_202_1,
//                    R.layout.custom_native_admob_large,
//                    object : AppLovinCallback() {
//                        override fun onAdLoaded() {
//                            super.onAdLoaded()
//                            isL2NativePreloaded = true;
//                            checkAndShowContinueButton()
//                        }
//
//                        override fun onAdFailedToLoad(i: MaxError?) {
//                            super.onAdFailedToLoad(i)
//                            isL2NativePreloaded = true;
//                            checkAndShowContinueButton()
//                        }
//
//                        override fun onAdClicked() {
//                            super.onAdClicked()
//                            Language2Activity.clickNative = true;
//                        }
//                    })
//            } else {
//                var listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
//                    AdsNativeMultiPreload.AdIdModel().apply {
//                        adId = RemoteConfigManager.instance!!.getLfo2HighAdId()
//                        adName = "native_language_2_high"
//                    }
//                )
//                if (RemoteConfigManager.instance!!.getListAdIsOrder202()[0].equals("All")) {
//                    listAdId = listOf(
//                        AdsNativeMultiPreload.AdIdModel().apply {
//                            adId = RemoteConfigManager.instance!!.getLfo2AdId()
//                            adName = "native_language_2"
//                        }
//                    )
//                }
//
//                AdsNativeMultiPreload.preloadMultipleNativeAds(
//                    this@SplashActivity,
//                    YNMAirBridge.AppData(nameView, ObdConstants.NATIVE_LANGUAGE2),
//                    listAdId,
//                    ObdConstants.PRELOAD_NATIVE_202_1,
//                    object : YNMAdsCallbacks() {
//                        override fun onAdClicked() {
//                            super.onAdClicked()
//                            Language2Activity.clickNative = true;
//                        }
//
//                        override fun onNativeAdLoaded(nativeAd: NativeAd) {
//                            super.onNativeAdLoaded(nativeAd)
//                            isL2NativePreloaded = true;
//                            checkAndShowContinueButton()
//                        }
//
//                        override fun onAdFailedToLoad(adError: AdsError?) {
//                            super.onAdFailedToLoad(adError)
//                            isL2NativePreloaded = true;
//                            checkAndShowContinueButton()
//                        }
//                    }
//                )
//            }
//        }
    }

    private fun preloadSplashInterstial() {
        val listAdId: List<AdsInterMultiPreload.AdIdModel> = listOf(
            AdsInterMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getInterHighSplAdId()
                adName = "inter_splash_high"
                retryCount = 1
                delayTimeBeforeRetry = 300
            },
            AdsInterMultiPreload.AdIdModel().apply {
                adId = RemoteConfigManager.instance!!.getInterSplAdId()
                adName = "inter_splash"
            }
        )
        if (RemoteConfigManager.instance!!.retryHigh == false) {
            listAdId[0].retryCount = 0
            listAdId[0].delayTimeBeforeRetry = 0
        }
        AdsInterMultiPreload.preloadMultipleInterAds(
            this@SplashActivity,
            YNMAirBridge.AppData(nameView, "inter_splash"),
            listAdId,
            ObdConstants.INTER_SPLASH,
            object : YNMAdsCallbacks(YNMAirBridge.AppData(nameView, ObdConstants.INTER_SPLASH), YNMAds.INTERSTITIAL) {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Log.d("SplashActivity", "Interstitial ad preloaded successfully for splash")
                    isSplashInterPreloaded = true
                    checkAndShowContinueButton()
                }
                
                override fun onAdFailedToLoad(adError: AdsError?) {
                    super.onAdFailedToLoad(adError)
                    Log.e("SplashActivity", "Failed to preload interstitial ad: ${adError?.message}")
                    isSplashInterPreloaded = true // Consider failed as loaded to not block UI
                    checkAndShowContinueButton()
                }
            }
        )
    }

    private fun onTimeoutReached() {
        Log.d("SplashActivity", "Timeout reached")
        checkAndShowContinueButton()
    }
    
    private fun checkAndShowContinueButton() {
        Log.d("SplashActivity", "Checking conditions - Progress: $isProgressCompleted, Native: $isSplashNativeLoaded, Inter: $isSplashInterPreloaded, L1: $isL1NativePreloaded")
        
        // Check if we should show continue button
        val shouldShowButton = isProgressCompleted || 
                (isSplashNativeLoaded && isSplashInterPreloaded && isL1NativePreloaded && isL2NativePreloaded)

        Log.d("SplashActivity", "shouldShowButton: $shouldShowButton isProgressCompleted: $isProgressCompleted isSplashNativeLoaded: $isSplashNativeLoaded isSplashInterPreloaded: $isSplashInterPreloaded isL1NativePreloaded: $isL1NativePreloaded")
        
        if (shouldShowButton && !canShowContinueButton) {
            canShowContinueButton = true
            showContinueButton()
        }
    }
    
    private fun showContinueButton() {
        Log.d("SplashActivity", "Showing continue button")
        
        // Force complete progress bar if not already completed
        if (!isProgressCompleted) {
            viewBinding?.splashProgressBar?.forceComplete()
        }
        
        // Show continue button (you'll need to add this button to your layout)
        viewBinding?.layoutContinue?.apply {
            visibility = View.VISIBLE
        }
        viewBinding?.btnContinue?.apply {
            setOnClickListener {
                elementClickEvent("btn_continue", "button")
                onContinueButtonClicked()
            }
        }
    }
    
    private fun onContinueButtonClicked() {
        Log.d("SplashActivity", "Continue button clicked")

        // Hide continue button
        viewBinding?.btnContinue?.visibility = View.GONE

        // Show interstitial ad
        if (!Utils.isDisableObdAd()) {
            showSplashInterstitialAd()
        } else {
            startNextActivity()
        }
    }
    
    private fun showSplashInterstitialAd() {
        AdsInterMultiPreload.showPreloadedInterAdWithLoading(
            this@SplashActivity,
            ObdConstants.INTER_SPLASH,
            10000,
            object : YNMAdsCallbacks(YNMAirBridge.AppData(nameView, ObdConstants.INTER_SPLASH), YNMAds.INTERSTITIAL) {
                override fun onNextAction(isShown: Boolean) {
                    super.onNextAction(isShown)
                    startNextActivity()
                    isStartNextActivityCalled = true
                }

                override fun onInterstitialShow() {
                    super.onInterstitialShow()
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    // Clean up after showing
                    AdsInterMultiPreload.destroyPreloadedAd(ObdConstants.INTER_SPLASH)
                }

                override fun onAdFailedToLoad(adError: AdsError?) {
                    super.onAdFailedToLoad(adError)
                    startNextActivity()
                }
            }
        )
    }

    private fun startNextActivity() {
        if (isStartNextActivityCalled) return
        isStartNextActivityCalled = true

        viewBinding?.splashProgressBar?.end()

        // Check if opened from Word of the Day

        RemoteConfigManager.instance!!.loadConfigCallback(this,object : RemoteConfigManager.BooleanCallback {
            override fun onResult(value: Boolean) {
                if (!Utils.isFinishObd() && !Utils.isDisableObdAd()) {
                    if (ObdConstants.TEST_1_ONBOARDING) {
                        SharedPref.saveString(AppConstants.LANG_CODE_STORE, "en")
                        val mainIntent = Intent(this@SplashActivity, Language2Activity::class.java)
                        startActivity(mainIntent)
                        return
                    }
                    val mainIntent = Intent(this@SplashActivity, LanguageActivity::class.java)
                    startActivity(mainIntent)
                } else {
                    YNMAds.getInstance().adConfig.setInterFlow(RemoteConfigManager.instance!!.getStartIndexInter(), RemoteConfigManager.instance!!.getDeltaIndexInter())
                    YNMAds.isGoHome = true;
                    val bundle = Bundle().apply {
                        putString(AppConstants.OPEN_FROM_SCREEN_KEY, nameView)
                    }
                    AppExtension.goHomeActivity(this@SplashActivity, bundle)
                }
            }
        })
    }


    private fun showNoNetworkDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again")
            .setCancelable(false)
            .setPositiveButton("Retry") { _: DialogInterface, _: Int ->
                // Kiểm tra lại kết nối khi nhấn Thử lại
                if (InternetUtil.isNetworkAvailable(this@SplashActivity)) {
                    initAction()
                } else {
                    // Vẫn không có mạng, hiển thị lại popup
                    showNoNetworkDialog()
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun handleNotificationClick(intent: Intent?) {
        if (intent?.extras != null) {
            val notificationId = intent.getStringExtra("notification_id")
            if (notificationId != null) {
                val params = Bundle()
                params.putString("notification_id", notificationId)
//                Utils.isOpenFromNoti = true
//                Utils.notiId = notificationId
                YNMAirBridge.getInstance().logCustomEvent("notification_clicked", "", notificationId)
                YNMAirBridge.getInstance().logCustomEvent("open_from_noti", "", notificationId)
                Log.d("NOTI", "tu noti ne: $notificationId")
                YNMAirBridge.setTagTest("noti")
            }
        }
    }

    private fun setDefaultLanguageBasedOnDeviceLocale() {
        // Get device locale
        var deviceLocale = getDeviceLanguage()
        // List of supported languages from LanguageModel
        val supportedLanguages = listOf("en", "es", "pt", "hi", "ar", "vi", "fr", "ja", "ko", "it")
        
        // Check if device language is supported
        val defaultLang = if (supportedLanguages.contains(deviceLocale)) {
            deviceLocale
        } else {
            "en" // Default to English if device language not supported
        }
        
        // Save the detected language as default (only if no language is already set)
        val currentSavedLang = SharedPref.readString(AppConstants.LANG_CODE_STORE, "")
        if (currentSavedLang.isEmpty()) {
            SharedPref.saveString(AppConstants.LANG_CODE_STORE, defaultLang)

            Log.d("SplashActivity", "Default language set to: $defaultLang based on device locale: ${Locale.getDefault().language}")

            // Update locale and text immediately without recreating activity
            updateLocaleAndText(defaultLang)
        }
    }
    
    private fun updateLocaleAndText(language: String) {
        // Update locale for current activity
        val localizedContext = LocaleHelper.setLocale(this, language)
        
        // Update button text using localized context
        viewBinding?.btnContinue?.text = localizedContext.getString(R.string.continue_btn)
        viewBinding?.appNameText?.text = localizedContext.getString(R.string.title_splash)
        
        Log.d("SplashActivity", "Updated locale and text to language: $language")
    }


    override fun attachBaseContext(newBase: Context?) {
        val savedLang = SharedPref.readString(AppConstants.LANG_CODE_STORE, "en")
        val context = if (newBase != null) LocaleHelper.setLocale(newBase, savedLang) else newBase
        super.attachBaseContext(context)
    }

    override fun onDestroy() {
        // Hủy timeout handler khi activity bị destroy
        timeoutHandler.removeCallbacks(timeoutRunnable)
        super.onDestroy()
        AdsNativeMultiPreload.destroyPreloadedAd(ObdConstants.NATIVE_SPLASH)
    }

    override fun onResume() {
        super.onResume()
        if (isNativeSplashClicked && !isStartNextActivityCalled) {
            showSplashNativeAds();
        }
    }

    fun getDeviceLanguage(): String {
        val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            Resources.getSystem().configuration.locale
        }

        return locale.language
    }
}
