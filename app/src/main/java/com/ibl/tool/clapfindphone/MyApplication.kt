package com.ibl.tool.clapfindphone

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.ads.nomyek_admob.admobs.Admob
import com.ads.nomyek_admob.admobs.AppOpenManager
import com.ads.nomyek_admob.ads_components.YNMAds
import com.ads.nomyek_admob.application.AdsApplication
import com.ads.nomyek_admob.config.AirBridgeConfig
import com.ads.nomyek_admob.config.YNMAdsConfig
import com.facebook.FacebookSdk
import com.facebook.ads.AudienceNetworkAds
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessaging
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.app.ObdConstants
import com.ibl.tool.clapfindphone.data.db.RoomDatabase
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.utils.SharedPref
import com.ibl.tool.clapfindphone.utils.Utils
import com.ibl.tool.clapfindphone.utils.app.AppPreferences
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.MBridgeSDK
import com.mbridge.msdk.out.MBridgeSDKFactory
import kotlin.to


class MyApplication : AdsApplication() {
    override fun onCreate() {
        super.onCreate()
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
        airBridgeConfig.appNameAirBridge = "clapfindphone"
        airBridgeConfig.tokenAirBridge = "28c928a41ec24693b12ac6c5089779d3"
        airBridgeConfig.userState = Utils.getUserState();
        Utils.setFirstOpenApp(false);
        this.ynmAdsConfig.airBridgeConfig = airBridgeConfig
        this.ynmAdsConfig.setAdTrackingList(
            listOf(
                YNMAdsConfig.AdItem(BuildConfig._101_spl_inter_high, ObdConstants.INTER_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._101_v2_spl_inter_high, ObdConstants.INTER_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._102_spl_native_high, ObdConstants.NATIVE_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._102_v2_spl_native_high, ObdConstants.NATIVE_SPLASH_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._201_lfo_native_high, ObdConstants.NATIVE_LANGUAGE1_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._202_lfo_native_high, ObdConstants.NATIVE_LANGUAGE2_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._301_onb_native_high, ObdConstants.NATIVE_OB1_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._302_onb_native_high, ObdConstants.NATIVE_OB2_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._304_onb_native_high, ObdConstants.NATIVE_OB4_HIGHFLOOR),
                YNMAdsConfig.AdItem(BuildConfig._305_onb_native_high, ObdConstants.NATIVE_OB5_HIGHFLOOR),
            )
        )
        YNMAdsConfig.AD_TRACKING_GROUPS = mapOf(
            "splash_highfloor_pass" to listOf(
                ObdConstants.INTER_SPLASH_HIGHFLOOR,
                ObdConstants.NATIVE_SPLASH_HIGHFLOOR
            ),
            "language_highfloor_pass" to listOf(
                ObdConstants.NATIVE_LANGUAGE1_HIGHFLOOR,
                ObdConstants.NATIVE_LANGUAGE2_HIGHFLOOR
            ),
            "onboard_highfloor_pass" to listOf(
                ObdConstants.NATIVE_OB1_HIGHFLOOR,
                ObdConstants.NATIVE_OB2_HIGHFLOOR,
                ObdConstants.NATIVE_OB4_HIGHFLOOR,
                ObdConstants.NATIVE_OB5_HIGHFLOOR
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

//        private fun initRemoteConfig(context: Context) {
//            remoteConfig = Firebase.remoteConfig
//            val configSettings = remoteConfigSettings {
//                minimumFetchIntervalInSeconds = 0
//            }
//            remoteConfig.setConfigSettingsAsync(configSettings)
//
//            // Set default values (make sure they match your XML defaults)
//            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
//        }
    }
}