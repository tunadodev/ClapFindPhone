package com.puto.tool.clapfindphone

import android.content.Context
import android.util.Log
import com.ads.nomyek_admob.admobs.Admob
import com.ads.nomyek_admob.ads_components.YNMAds
import com.ads.nomyek_admob.application.AdsApplication
import com.ads.nomyek_admob.config.AirBridgeConfig
import com.ads.nomyek_admob.config.YNMAdsConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessaging
import com.puto.tool.clapfindphone.app.AppConstants
import com.puto.tool.clapfindphone.data.db.RoomDatabase
import com.puto.tool.clapfindphone.navigation.AppNavigatorImpl
import com.puto.tool.clapfindphone.utils.app.AppPreferences
import com.jrm.onboarding.navigation.BaseNavigator
import com.jrm.utils.BaseConstants
import com.jrm.utils.BaseUtils
import com.jrm.utils.SharedPref
import com.jrm.utils.remote_config.RemoteConfigManager
import kotlin.to


class MyApplication : AdsApplication() {
    override fun onCreate() {
        super.onCreate()
        // Initialize navigation
        BaseNavigator.setInstance(AppNavigatorImpl())
        SharedPref.init(this)
        FirebaseApp.initializeApp(this)
        initFirebaseAnalytics(this)
        initializeContext(this)
        //initRemoteConfig(this)
        SharedPref.saveBoolean(AppConstants.ENABLE_ADS, true);

        RemoteConfigManager.instance?.loadRemote()
        RoomDatabase.initDatabase(this)
        AppPreferences(this)
//        Purchases.logLevel = LogLevel.DEBUG
//        Purchases.configure(PurchasesConfiguration.Builder(this, "goog_qmMaclJABrdadUGPCgweMGCuZoc").build())
        // init ads
        val environment =
            if (BuildConfig.env_dev) YNMAdsConfig.ENVIRONMENT_DEVELOP else YNMAdsConfig.ENVIRONMENT_PRODUCTION
        this.ynmAdsConfig = YNMAdsConfig(this, YNMAdsConfig.PROVIDER_ADMOB, environment)
        // Optional: setup Airbridge
        val airBridgeConfig = AirBridgeConfig()
        airBridgeConfig.isEnableAirBridge = true
        airBridgeConfig.appNameAirBridge = "clap2"
        airBridgeConfig.tokenAirBridge = "8f9574744c884a708493d1ec5db0e170"
        airBridgeConfig.userState = BaseUtils.getUserState();
        BaseUtils.setFirstOpenApp(false);
        this.ynmAdsConfig.airBridgeConfig = airBridgeConfig
        this.ynmAdsConfig.setAdTrackingList(
            listOf(
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._101_spl_inter_high, BaseConstants.INTER_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._101_v2_spl_inter_high, BaseConstants.INTER_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._102_spl_native_high, BaseConstants.NATIVE_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._102_v2_spl_native_high, BaseConstants.NATIVE_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._201_lfo_native_high, BaseConstants.NATIVE_LANGUAGE1_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._202_lfo_native_high, BaseConstants.NATIVE_LANGUAGE2_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._301_onb_native_high, BaseConstants.NATIVE_OB1_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._302_onb_native_high, BaseConstants.NATIVE_OB2_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._304_onb_native_high, BaseConstants.NATIVE_OB4_HIGHFLOOR),
                YNMAdsConfig.AdItem(com.jrm.BuildConfig._305_onb_native_high, BaseConstants.NATIVE_OB5_HIGHFLOOR),
            )
        )
        YNMAdsConfig.AD_TRACKING_GROUPS = mapOf(
            "splash_highfloor_pass" to listOf(
                BaseConstants.INTER_SPLASH_HIGHFLOOR,
                BaseConstants.NATIVE_SPLASH_HIGHFLOOR
            ),
            "language_highfloor_pass" to listOf(
                BaseConstants.NATIVE_LANGUAGE1_HIGHFLOOR,
                BaseConstants.NATIVE_LANGUAGE2_HIGHFLOOR
            ),
            "onboard_highfloor_pass" to listOf(
                BaseConstants.NATIVE_OB1_HIGHFLOOR,
                BaseConstants.NATIVE_OB2_HIGHFLOOR,
                BaseConstants.NATIVE_OB4_HIGHFLOOR,
                BaseConstants.NATIVE_OB5_HIGHFLOOR
            )
        )
        // Optional: enable ads resume
        this.ynmAdsConfig.idAdResume = ""
        // Optional: setup list device test - recommended to use
        this.listTestDevice.add("EC25F576DA9B6CE74778B268CB87E431")
        this.ynmAdsConfig.listDeviceTest = this.listTestDevice
        this.ynmAdsConfig.maxKey = BuildConfig.key_max
        this.ynmAdsConfig.intervalInterstitialAd = 25

        YNMAds.getInstance().init(null, this, this.ynmAdsConfig)

        // Auto disable ad resume after user click ads and back to app
        Admob.getInstance().setDisableAdResumeWhenClickAds(true)

        // If true -> onNextAction() is called right after Ad Interstitial showed
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)


    }

    companion object {
        private lateinit var firebaseAnalytics: FirebaseAnalytics
        private lateinit var contextApp: AdsApplication


        fun getFireBaseAnalytic(): FirebaseAnalytics {
            return firebaseAnalytics
        }

        fun getContext(): AdsApplication {
            return contextApp
        }

//        fun getRemoteConfig(): FirebaseRemoteConfig {
//            return remoteConfig
//        }

        private fun initializeContext(context: AdsApplication) {
            contextApp = context
        }

        private fun initFirebaseAnalytics(context: Context): FirebaseAnalytics {
            if (!::firebaseAnalytics.isInitialized) {
                firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            }
            return firebaseAnalytics
        }

        private fun FcmTestGetKey(){
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                Log.d("Fcm :", token);
            })
        }
    }
}