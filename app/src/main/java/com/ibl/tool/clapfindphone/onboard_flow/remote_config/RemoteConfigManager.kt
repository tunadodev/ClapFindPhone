package com.ibl.tool.clapfindphone.onboard_flow.remote_config

import android.app.Activity
import android.os.Build
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.onboard_flow.utils.ObdUtils
import com.ibl.tool.clapfindphone.utils.InternetUtil

class RemoteConfigManager {
    private var remoteConfig: FirebaseRemoteConfig? = null
    private var isLoading = false
    var numberScreenObd: Long = 5;
    var timeReloadBanner: Long = 30;
    var timeReloadNative: Long = 30;
    var timeOutSplash: Long = 10000
    var retryHigh: Boolean = true
    var languageOrder: String = ""
    var disableObdAds: Boolean = false
    var disableAllAds: Boolean = false
    var adIdsOrder202: String = "Max,High,All"
    var adIdsOrder303: String = "Max,High,All"
    
    // New remote config flags for ads
    var interSpl: Boolean = true
    var interHighSpl: Boolean = true
    var nativeSpl: Boolean = true
    var nativeHighSpl: Boolean = true
    var bannerHighSpl: Boolean = true
    var bannerSpl: Boolean = true
    var lfo1: Boolean = true
    var lfo1High: Boolean = true
    var lfo2: Boolean = true
    var lfo2High: Boolean = true
    var lfo2Max: Boolean = true
    var ob1: Boolean = true
    var ob1High: Boolean = true
    var ob2: Boolean = true
    var ob2High: Boolean = true
    var ob2Max: Boolean = true
    var ob4: Boolean = true
    var ob4High: Boolean = true
    var ob5: Boolean = true
    var ob5High: Boolean = true
    var ob6: Boolean = true
    var ob6High: Boolean = true
    var interstitialRule: String = "1/2"
    var formatTypeSplash = "native"
    var preloadInterFinishObdIndex: Long = 3

    var zipAsset: String = ""
    var chainAsset: String = ""
    var backgroundAsset: String = ""

    var timeReloadNativeBanner: Long = 30
    
    var nativeThemeListStart: Long = 2
    var nativeThemeListDelta: Long = 3
    var enablePreloadReward: Boolean = false
    var enableReward: Boolean = true

    fun loadRemote() {
        if (isLoading) {
            return
        }
        isLoading = true
        val config = FirebaseRemoteConfig.getInstance()
        val configSettings =
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build()
        config.setConfigSettingsAsync(configSettings)
        config.setDefaultsAsync(R.xml.default_config)
        config.fetch().addOnCompleteListener {
            FirebaseRemoteConfig.getInstance().activate().addOnCompleteListener {
                isLoading = false
                remoteConfig = FirebaseRemoteConfig.getInstance()
                numberScreenObd = config.getLong(NUMBER_OBD_SCREEN)
                timeReloadBanner = config.getLong("time_reload_banner")
                timeReloadNative = config.getLong("time_reload_native")
                timeOutSplash = config.getLong("time_out_splash")
                retryHigh = config.getBoolean("retry_high")
                languageOrder = config.getString("language_order")
                disableAllAds = config.getBoolean("disable_all_ads")
                disableObdAds = config.getBoolean("disable_obd_ads")
                
                // Load new remote config flagss
                interSpl = config.getBoolean("inter_spl")
                interHighSpl = config.getBoolean("inter_high_spl")
                nativeSpl = config.getBoolean("native_spl")
                nativeHighSpl = config.getBoolean("native_high_spl")
                bannerSpl = config.getBoolean("banner_spl")
                bannerHighSpl = config.getBoolean("banner_high_spl")
                lfo1 = config.getBoolean("lfo1")
                lfo1High = config.getBoolean("lfo1_high")
                lfo2 = config.getBoolean("lfo2")
                lfo2High = config.getBoolean("lfo2_high")
                lfo2Max = config.getBoolean("lfo2_max")
                ob1 = config.getBoolean("ob1")
                ob1High = config.getBoolean("ob1_high")
                ob2 = config.getBoolean("ob2")
                ob2High = config.getBoolean("ob2_high")
                ob2Max = config.getBoolean("ob2_max")
                ob4 = config.getBoolean("ob4")
                ob4High = config.getBoolean("ob4_high")
                ob5 = config.getBoolean("ob5")
                ob5High = config.getBoolean("ob5_high")
                ob6 = config.getBoolean("ob6")
                ob6High = config.getBoolean("ob6_high")
                adIdsOrder202 = config.getString("ad_ids_order_202")
                adIdsOrder303 = config.getString("ad_ids_order_303")
                interstitialRule = config.getString("interstitial_rule")
                formatTypeSplash = config.getString("format_splash")
                preloadInterFinishObdIndex = config.getLong("preload_inter_obd_index")
                zipAsset = config.getString("zip_asset")
                chainAsset = config.getString("chain_asset")
                backgroundAsset = config.getString("background_asset")
                timeReloadNativeBanner = config.getLong("time_reload_native_banner")
                nativeThemeListStart = config.getLong("native_theme_list_start")
                nativeThemeListDelta = config.getLong("native_theme_list_delta")
                enablePreloadReward = config.getBoolean("enable_preload_reward")
                enableReward = config.getBoolean("enable_reward")

            }
        }
    }

    fun getListAdIsOrder202(): List<String> {
        val listAdIds = adIdsOrder202.split(",")
        return listAdIds
    }

    fun getListAdIsOrder303(): List<String> {
        val listAdIds = adIdsOrder303.split(",")
        return listAdIds
    }

    // Functions to get Ad IDs based on remote config flags
    fun getInterSplAdId(): String {
        Log.d("Remote Config", "getInterSplAdId: " + (if (interSpl) BuildConfig._101_spl_inter else ""))
        Log.d("Remote Config", "getInterSplAdId: " + (if (ObdUtils.getSessionNumber() > 1) "spl_ss2" else "spl_ss1"))
        return if (interSpl) (if (ObdUtils.getSessionNumber() > 1) BuildConfig._101_v2_spl_inter else BuildConfig._101_spl_inter) else ""
    }

    fun getInterHighSplAdId(): String {
        return if (interHighSpl) (if (ObdUtils.getSessionNumber() > 1) BuildConfig._101_v2_spl_inter_high else BuildConfig._101_spl_inter_high) else ""
    }

    fun getNativeSplAdId(): String {
        return if (nativeSpl) (if (ObdUtils.getSessionNumber() > 1) BuildConfig._102_v2_spl_native else BuildConfig._102_spl_native) else ""
    }

    fun getNativeHighSplAdId(): String {
        return if (nativeHighSpl) (if (ObdUtils.getSessionNumber() > 1) BuildConfig._102_v2_spl_native_high else BuildConfig._102_spl_native_high) else ""
    }

    fun getBannerSplAdId() : String {
        return if (bannerSpl) (if (ObdUtils.getSessionNumber() > 1) BuildConfig._103_v2_spl_banner else BuildConfig._103_spl_banner) else ""
    }

    fun getBannerHighSplAdId() : String {
        return if (bannerHighSpl) (if (ObdUtils.getSessionNumber() > 1) BuildConfig._103_v2_spl_banner_high else BuildConfig._103_spl_banner_high) else ""
    }

    fun getLfo1AdId(): String {
        return if (lfo1) BuildConfig._201_lfo_native else ""
    }

    fun getLfo1HighAdId(): String {
        return if (lfo1High) BuildConfig._201_lfo_native_high else ""
    }

    fun getLfo2AdId(): String {
        return if (lfo2) BuildConfig._202_lfo_native else ""
    }

    fun getLfo2HighAdId(): String {
        return if (lfo2High) BuildConfig._202_lfo_native_high else ""
    }

    fun getLfo2MaxAdId(): String {
        return if (lfo2Max) BuildConfig._202_lfo_native_max else ""
    }

    fun getOb1AdId(): String {
        return if (ob1) BuildConfig._301_onb_native else ""
    }

    fun getOb1HighAdId(): String {
        return if (ob1High) BuildConfig._301_onb_native_high else ""
    }

    fun getOb2AdId(): String {
        return if (ob2) BuildConfig._302_onb_native else ""
    }

    fun getOb2HighAdId(): String {
        return if (ob2High) BuildConfig._302_onb_native_high else ""
    }

    fun getOb2MaxAdId(): String {
        return if (ob2Max) BuildConfig._302_onb_native_max else ""
    }

    fun getOb4AdId(): String {
        return if (ob4) BuildConfig._304_onb_native else ""
    }

    fun getOb4HighAdId(): String {
        Log.d("Remote Config", "getOb4HighAdId: " + (if (ob4High) BuildConfig._304_onb_native_high else ""))
        return if (ob4High) BuildConfig._304_onb_native_high else ""
    }

    fun getOb5AdId(): String {
        return if (ob5) BuildConfig._305_onb_native else ""
    }

    fun getOb5HighAdId(): String {
        return if (ob5High) BuildConfig._305_onb_native_high else ""
    }

    fun getOb6AdId(): String {
        return if (ob6) BuildConfig._306_onb_inter else ""
    }

    fun getOb6HighAdId(): String {
        return if (ob6High) BuildConfig._306_onb_inter_high else ""
    }

    fun getStartIndexInter(): Int {
        return try {
            val parts = interstitialRule?.split("/") ?: return 0
            if (parts.size >= 1) {
                parts[0].toIntOrNull() ?: 0
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    fun getDeltaIndexInter(): Int {
        return try {
            val parts = interstitialRule?.split("/") ?: return 1
            if (parts.size >= 2) {
                parts[1].toIntOrNull() ?: 1
            } else {
                1
            }
        } catch (e: Exception) {
            1
        }
    }


    fun loadIsShowConsent(activity: Activity, callback: BooleanCallback) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!InternetUtil.isNetworkAvailable(activity)) {
//                callback.onResult(false)
//                return
//            }
//        }
        if (isLoading && remoteConfig == null) {
            Thread {
                while (isLoading || remoteConfig == null) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                activity.runOnUiThread {
                    if (remoteConfig != null) {
                        callback.onResult(remoteConfig!!.getBoolean(IS_SHOW_CONSENT))
                    }
                }
            }.start()
        } else {
            if (remoteConfig != null) {
                callback.onResult(remoteConfig!!.getBoolean(IS_SHOW_CONSENT))
            }
        }
    }


    fun loadReshowGDPRSplashCount(activity: Activity, callback: NumberCallback) {
        if (isLoading && remoteConfig == null) {
            Thread {
                while (isLoading || remoteConfig == null) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                activity.runOnUiThread { callback.onResult(remoteConfig!!.getLong(RESHOW_GDPR_SPLASH)) }
            }.start()
        } else {
            callback.onResult(remoteConfig!!.getLong(RESHOW_GDPR_SPLASH))
        }
    }

    val isShowConsent: Boolean
        get() = remoteConfig != null && remoteConfig!!.getBoolean(IS_SHOW_CONSENT)

    fun limitFunctionClickCount(): Long {
        return if (remoteConfig == null) {
            0
        } else {
            remoteConfig!!.getLong(LIMIT_FUNCTION_IN_APP)
        }
    }

    fun getLanguageOrderList(): List<String> {
        return if (languageOrder.isNotEmpty()) {
            try {
                languageOrder.split(",").map { it.trim() }
            } catch (e: Exception) {
                Log.e("RemoteConfigManager", "Error parsing language order: ${e.message}")
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun isEnableReward(): Boolean {
        return enableReward
    }

    fun loadTimeOutSplash(activity: Activity, callback: com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager.NumberCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!InternetUtil.isNetworkAvailable(activity)) {
                callback.onResult(10000)
                return
            }
        }
        if (isLoading && remoteConfig == null) {
            Thread {
                while (isLoading || remoteConfig == null) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                activity.runOnUiThread {
                    if (remoteConfig != null) {
                        callback.onResult(remoteConfig!!.getLong("time_out_splash"))
                    }
                }
            }.start()
        } else {
            if (remoteConfig != null) {
                callback.onResult(remoteConfig!!.getLong("time_out_splash"))
            }
        }
    }

    fun loadConfigCallback(activity: Activity, callback: com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager.BooleanCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!InternetUtil.isNetworkAvailable(activity)) {
                callback.onResult(false)
                return
            }
        }
        if (isLoading && remoteConfig == null) {
            Thread {
                while (isLoading || remoteConfig == null) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
                activity.runOnUiThread {
                    if (remoteConfig != null) {
                        callback.onResult(true)
                    }
                }
            }.start()
        } else {
            if (remoteConfig != null) {
                callback.onResult(true)
            }
        }
    }

    interface BooleanCallback {
        fun onResult(value: Boolean)
    }

    interface NumberCallback {
        fun onResult(value: Long)
    }

    interface StringCallback {
        fun onResult(value: String?)
    }

    companion object {
        private const val IS_SHOW_CONSENT = "is_show_consent"
        private const val LIMIT_FUNCTION_IN_APP = "limit_function_in_app"
        private const val RESHOW_GDPR_SPLASH = "reshow_gdpr_splash"
        private const val NUMBER_OBD_SCREEN = "obd_number_screen"
        private var INSTANCE: RemoteConfigManager? = null

        @JvmStatic
        val instance: RemoteConfigManager?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = RemoteConfigManager()
                }
                return INSTANCE
            }
    }
}