package com.ibl.tool.clapfindphone.onboard_flow.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ads.nomyek_admob.admobs.AppOpenManager
import com.ads.nomyek_admob.ads_components.YNMAds
import com.ads.nomyek_admob.ads_components.YNMAdsCallbacks
import com.ads.nomyek_admob.ads_components.ads_banner.YNMBannerAdView
import com.ads.nomyek_admob.ads_components.ads_native.YNMNativeAdView
import com.ads.nomyek_admob.ads_components.wrappers.AdsError
import com.ads.nomyek_admob.event.YNMAirBridge
import com.ads.nomyek_admob.event.YNMAirBridgeDefaultEvent
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.google.android.gms.ads.nativead.NativeAd
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.app.AppConstants
import com.ibl.tool.clapfindphone.app.ObdConstants
import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager
import com.ibl.tool.clapfindphone.utils.LocaleHelper
import com.ibl.tool.clapfindphone.utils.SharedPref
import com.ibl.tool.clapfindphone.utils.Utils
import com.ibl.tool.clapfindphone.utils.firebaseAnalyticsEvent


public abstract class BaseObdActivity<VB : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var viewBinding: VB
    public var nameView: String? = null
    private var listBannerId: List<String> = listOf()
    private var refreshBannerTime: Int = 0
    private var refreshNativeBannerTime: Long = 0
    private var refreshHandler: Handler? = null
    private var refreshRunnable: Runnable? = null
    private var refreshNativeHandler: Handler? = null
    private var refreshNativeRunnable: Runnable? = null
    private var refreshNativeMediumHandler: Handler? = null
    private var refreshNativeMediumRunnable: Runnable? = null
    private var refreshNativeMediumTime: Long = 0
    private var isInForeground: Boolean = true
    private var startScreenTime = 0L
    private var openFromScreen = "null"
    private var typeBanner: String = "normal"

    fun setListBannerId(bannerIds: List<String>) {
        this.listBannerId = bannerIds
    }

    fun setRefreshBannerTime(timeInSeconds: Int) {
        this.refreshBannerTime = timeInSeconds
    }

    fun setRefreshNativeBannerTime(timeInSeconds: Long) {
        this.refreshNativeBannerTime = timeInSeconds
    }

    fun setRefreshNativeMediumTime(timeInSeconds: Long) {
        this.refreshNativeMediumTime = timeInSeconds
    }

    fun setBannerType(type: String) {
        this.typeBanner = type;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initWindow(window)
        fullScreenCall(window)
        super.onCreate(savedInstanceState)

        // Initialize the view binding
        viewBinding = DataBindingUtil.setContentView(this, getLayoutActivity())
        setContentView(viewBinding.root)
        openFromScreen = intent.getStringExtra(AppConstants.OPEN_FROM_SCREEN_KEY) ?: "null"
        setRefreshNativeBannerTime(RemoteConfigManager.instance!!.timeReloadNativeBanner)
        setRefreshNativeMediumTime(RemoteConfigManager.instance!!.timeReloadNative)
        initViews()

        // Add padding to the top to avoid status bar hiding content
        findViewById<View>(android.R.id.content).let { contentView ->
            ViewCompat.setOnApplyWindowInsetsListener(contentView) { v, insets ->
                val topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
                val paddingTop = topInset.coerceAtLeast(50) // Ensure padding top is at least 50 or the status bar height
                v.setPadding(0, paddingTop, 0, 0)
                v.setBackgroundColor(resources.getColor(R.color.white))
                insets
            }
        }

        if (nameView != null) {
            firebaseAnalyticsEvent(baseContext, nameView!!, "view", "view")
            try {
                YNMAds.getInstance().setInitCallback {
                    YNMAirBridgeDefaultEvent.pushEventScreenView(YNMAirBridge.AppData(nameView, ""))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    protected abstract fun getLayoutActivity(): Int

    protected abstract fun initViews()

    fun loadCollapsibleBanner(id:String, gravity: String? = "bottom") {
        findViewById<View>(R.id.bannerView)?.let {
            if (!Utils.isDisableAllAd()) {
                YNMAds.getInstance().setInitCallback {
                    YNMAds.getInstance().loadCollapsibleBanner(this, id, gravity, YNMAdsCallbacks(YNMAirBridge.AppData(nameView, "banner"), YNMAds.BANNER))
                }
            } else {
                it.visibility = View.GONE
            }
        }
    }

    fun loadCollapsibleBanner(id:String) {
        findViewById<View>(R.id.bannerView)?.let {
            if (!Utils.isDisableAllAd()) {
                YNMAds.getInstance().setInitCallback {
                    YNMAds.getInstance().loadCollapsibleBanner(this, id, "bottom",
                        YNMAdsCallbacks(YNMAirBridge.AppData(nameView, "banner"), YNMAds.BANNER)
                    )
                }
            } else {
                it.visibility = View.GONE
            }
        }
    }

    fun loadBanner(id:String) {
        findViewById<View>(R.id.bannerView)?.let {
            if (!Utils.isDisableAllAd()) {
                YNMAds.getInstance().setInitCallback {
                    findViewById<YNMBannerAdView>(R.id.bannerView).loadBanner(this, id,object : YNMAdsCallbacks(YNMAirBridge.AppData(nameView, "banner_home"), YNMAds.BANNER) {
                        override fun onAdLoaded() {
                            super.onAdLoaded()
                        }

                        override fun onAdFailedToLoad(adError: AdsError?) {
                            super.onAdFailedToLoad(adError)
                        }
                    })
                }
            } else {
                it.visibility = View.GONE
            }
        }
    }

    fun loadMultiIdBanner() {
        findViewById<View>(R.id.bannerView)?.let {
            if (!Utils.isDisableAllAd() && listBannerId.isNotEmpty()) {
                YNMAds.getInstance().setInitCallback {
                    loadBannerWithFallback(0)
                }
            } else {
                it.visibility = View.GONE
            }
        }
    }

    private fun loadBannerWithFallback(index: Int) {
        if (index >= listBannerId.size) {
            // All banner IDs failed, hide banner view
            findViewById<View>(R.id.bannerView)?.visibility = View.GONE
            return
        }

        val bannerId = listBannerId[index]
        if (typeBanner.compareTo("normal") == 0) {
            findViewById<YNMBannerAdView>(R.id.bannerView)?.loadBanner(
                this,
                bannerId,
                object : YNMAdsCallbacks(YNMAirBridge.AppData(nameView, "banner"), YNMAds.BANNER) {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        // Banner loaded successfully, stop trying other IDs
                    }

                    override fun onAdFailedToLoad(adError: AdsError?) {
                        super.onAdFailedToLoad(adError)
                        // Current banner failed, try next ID
                        loadBannerWithFallback(index + 1)
                    }
                }
            )
        } else {
            YNMAds.getInstance().loadCollapsibleBanner(this, bannerId, "bottom", object : YNMAdsCallbacks(YNMAirBridge.AppData(nameView, "banner"), YNMAds.BANNER) {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    // Banner loaded successfully, stop trying other IDs
                }

                override fun onAdFailedToLoad(adError: AdsError?) {
                    super.onAdFailedToLoad(adError)
                    // Current banner failed, try next ID
                    loadBannerWithFallback(index + 1)
                }
            })
        }

    }

    fun showRefreshMultiIdBanner() {
        // Load banner initially
        loadMultiIdBanner()

        // Start refresh timer if refreshBannerTime > 0
        if (refreshBannerTime > 0) {
            startBannerRefreshTimer()
        }
    }

    private fun startBannerRefreshTimer() {
        // Cancel existing timer if any
        stopBannerRefreshTimer()

        refreshHandler = Handler(Looper.getMainLooper())
        refreshRunnable = object : Runnable {
            override fun run() {
                // Only refresh if app is in foreground
                if (isInForeground) {
                    loadMultiIdBanner()
                }

                // Schedule next refresh
                refreshHandler?.postDelayed(this, (refreshBannerTime * 1000).toLong())
            }
        }

        // Start the timer
        refreshHandler?.postDelayed(refreshRunnable!!, (refreshBannerTime * 1000).toLong())
    }

    private fun stopBannerRefreshTimer() {
        refreshRunnable?.let { runnable ->
            refreshHandler?.removeCallbacks(runnable)
        }
        refreshRunnable = null
        refreshHandler = null
    }

    private fun startNativeBannerRefreshTimer() {
        // Cancel existing timer if any
        stopNativeBannerRefreshTimer()

        refreshNativeHandler = Handler(Looper.getMainLooper())
        refreshNativeRunnable = object : Runnable {
            override fun run() {
                // Only refresh if app is in foreground
                if (isInForeground) {
                    loadNativeBanner()
                }

                // Schedule next refresh
                refreshNativeHandler?.postDelayed(this, (refreshNativeBannerTime * 1000).toLong())
            }
        }

        // Start the timer
        refreshNativeHandler?.postDelayed(refreshNativeRunnable!!, (refreshNativeBannerTime * 1000).toLong())
    }

    private fun stopNativeBannerRefreshTimer() {
        refreshNativeRunnable?.let { runnable ->
            refreshNativeHandler?.removeCallbacks(runnable)
        }
        refreshNativeRunnable = null
        refreshNativeHandler = null
    }

    private fun startNativeMediumRefreshTimer(listAdId: List<AdsNativeMultiPreload.AdIdModel>, adPlace: String, layoutResId: Int = R.layout.custom_native_admob_medium) {
        // Cancel existing timer if any
        stopNativeMediumRefreshTimer()

        refreshNativeMediumHandler = Handler(Looper.getMainLooper())
        refreshNativeMediumRunnable = object : Runnable {
            override fun run() {
                // Only refresh if app is in foreground
                if (isInForeground) {
                    loadNativeMedium(listAdId, adPlace, layoutResId)
                }

                // Schedule next refresh
                refreshNativeMediumHandler?.postDelayed(this, (refreshNativeMediumTime * 1000).toLong())
            }
        }

        // Start the timer
        refreshNativeMediumHandler?.postDelayed(refreshNativeMediumRunnable!!, (refreshNativeMediumTime * 1000).toLong())
    }

    private fun stopNativeMediumRefreshTimer() {
        refreshNativeMediumRunnable?.let { runnable ->
            refreshNativeMediumHandler?.removeCallbacks(runnable)
        }
        refreshNativeMediumRunnable = null
        refreshNativeMediumHandler = null
    }

    fun showRefreshNativeBanner() {
        // Load native banner initially
        loadNativeBanner()

        // Start refresh timer if refreshNativeBannerTime > 0
        if (refreshNativeBannerTime > 0) {
            startNativeBannerRefreshTimer()
        }
    }

    fun showNative(listAdId: List<AdsNativeMultiPreload.AdIdModel>, adPlace: String, layoutResId: Int = R.layout.custom_native_admob_medium) {
        // Load native initially
        loadNativeMedium(listAdId, adPlace, layoutResId)

        // Start refresh timer if refreshNativeMediumTime > 0
        if (refreshNativeMediumTime > 0) {
            startNativeMediumRefreshTimer(listAdId, adPlace, layoutResId)
        }
    }

    companion object {
        fun initWindow(window: Window) {
            val background = ColorDrawable(Color.parseColor("#FFFFFF"))
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = window.context.resources.getColor(android.R.color.black)
            window.setBackgroundDrawable(background)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        fun fullScreenCall(window: Window) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
            LocaleHelper.setLocale(
                newBase,
                SharedPref.readString(AppConstants.LANG_CODE_STORE, "en")
            )
        )
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        openFromScreen = intent.getStringExtra(AppConstants.OPEN_FROM_SCREEN_KEY) ?: "null"
    }

    override fun onStart() {
        super.onStart()
        startScreenTime = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        isInForeground = true
    }

    override fun onPause() {
        super.onPause()
        isInForeground = false
    }

    override fun onStop() {
        super.onStop()
        pushShowTimeEvent(System.currentTimeMillis() - startScreenTime)
    }

    private fun pushShowTimeEvent(useTime: Long) {
        try {
            YNMAds.getInstance().setInitCallback {
                YNMAirBridge.getInstance().logCustomEvent(
                    "screen_view",
                    null,
                    nameView,
                    useTime,
                    mapOf<String, Any>(
                        "previous_screen" to openFromScreen
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBannerRefreshTimer()
        stopNativeBannerRefreshTimer()
        stopNativeMediumRefreshTimer()
    }

    fun elementClickEvent(id: String, buttonType: String) {
        val viewName = nameView ?: "null"
        try {
            YNMAds.getInstance().setInitCallback {
                YNMAirBridge.getInstance().logCustomEvent(
                    "element_click",
                    null,
                    id,
                    null,
                    mapOf<String, Any>(
                        "element_type" to buttonType,
                        "tag_screen" to viewName
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // Find the currently focused view in the activity
            val currentFocus = this.currentFocus

            // Check if the focused view is an EditText
            if (currentFocus is EditText) {
                val outRect = Rect()
                currentFocus.getGlobalVisibleRect(outRect)

                // If the touch event is outside the bounds of the focused EditText...
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    // ...clear its focus and hide the keyboard.
                    currentFocus.clearFocus()
                    hideKeyboard(currentFocus)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun hideKeyboard(view: android.view.View) {
        // Get the InputMethodManager from the context
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun loadNativeBanner() {
        if (!isInForeground || AppOpenManager.getInstance().isInterstitialShowing || isDestroyed || isFinishing || Utils.isDisableAllAd()) {
            if (Utils.isDisableAllAd()) {
                findViewById<YNMNativeAdView>(R.id.native_banner)?.visibility = View.INVISIBLE
            }
            return
        }
        YNMAds.getInstance().setInitCallback {
            val listAdId: List<AdsNativeMultiPreload.AdIdModel> = listOf(
                AdsNativeMultiPreload.AdIdModel().apply {
                    adId = BuildConfig._405_default_nativebn
                    adName = "native_banner"
                }
            )

            AdsNativeMultiPreload.preloadMultipleNativeAds(
                this,
                YNMAirBridge.AppData(nameView, AppConstants.NATIVE_BANNER),
                listAdId,
                AppConstants.NATIVE_BANNER,
                object : YNMAdsCallbacks() {
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        // Show the native ad in the native ad view if available
                        findViewById<YNMNativeAdView>(R.id.native_banner)?.let { adView ->
                            AdsNativeMultiPreload.showPreloadedNativeAd(
                                this@BaseObdActivity,
                                adView,
                                AppConstants.NATIVE_BANNER,
                                R.layout.custom_native_admob_banner,
                                R.layout.custom_native_admob_banner
                            )
                        }
                    }
                }
            )
        }
    }

    fun loadNativeMedium(listAdId: List<AdsNativeMultiPreload.AdIdModel>, adPlace: String, layoutResId: Int = R.layout.custom_native_admob_medium) {
        if (!isInForeground || AppOpenManager.getInstance().isInterstitialShowing || isDestroyed || isFinishing || Utils.isDisableAllAd()) {
            if (Utils.isDisableAllAd()) {
                findViewById<YNMNativeAdView>(R.id.nativeAd)?.visibility = View.GONE
            }
            return
        }

        YNMAds.getInstance().setInitCallback {
            AdsNativeMultiPreload.preloadMultipleNativeAds(
                this,
                YNMAirBridge.AppData(nameView, adPlace),
                listAdId,
                adPlace,
                object : YNMAdsCallbacks() {
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        // Show the native ad in the native ad view if available
                        findViewById<YNMNativeAdView>(R.id.nativeAd)?.let { adView ->
                            AdsNativeMultiPreload.showPreloadedNativeAd(
                                this@BaseObdActivity,
                                adView,
                                adPlace,
                                layoutResId,
                                layoutResId
                            )
                        }
                    }
                }
            )
        }
    }

    fun logEvent(eventName: String) {

    }
}