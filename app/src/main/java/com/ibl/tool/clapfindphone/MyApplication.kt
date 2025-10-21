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
import com.ibl.tool.clapfindphone.data.db.RoomDatabase
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.utils.SharedPref
import com.ibl.tool.clapfindphone.utils.Utils
import com.ibl.tool.clapfindphone.utils.app.AppPreferences
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.MBridgeSDK
import com.mbridge.msdk.out.MBridgeSDKFactory


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
        airBridgeConfig.appNameAirBridge = "easytranslateai"
        airBridgeConfig.tokenAirBridge = "6fb9738a36ab43ddb9c591aa0659365a"
        airBridgeConfig.userState = Utils.getUserState();
        Utils.setFirstOpenApp(false);
        this.ynmAdsConfig.airBridgeConfig = airBridgeConfig
        this.ynmAdsConfig.setAdTrackingList(
            listOf(
                YNMAdsConfig.AdItem(BuildConfig._101_spl_inter_high, "inter_splash_highfloor"),
                YNMAdsConfig.AdItem(BuildConfig._101_v2_spl_inter_high, "inter_splash_highfloor"),)
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