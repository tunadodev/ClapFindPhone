package com.ibl.tool.clapfindphone.onboard_flow.consent_dialog

import android.app.Activity
import android.util.Log
import com.ads.nomyek_admob.event.YNMAirBridge
import com.applovin.sdk.AppLovinPrivacySettings
import com.bytedance.sdk.openadsdk.api.PAGConstant
import com.google.ads.mediation.pangle.PangleMediationAdapter
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.out.MBridgeSDKFactory
import com.ibl.tool.clapfindphone.onboard_flow.consent_dialog.base.BaseDialogConsentManager
import com.ibl.tool.clapfindphone.utils.SharedPref
import com.ibl.tool.clapfindphone.utils.purchase.PurchaseManagerInApp

class ConsentDialogManager : BaseDialogConsentManager() {
    private var rejectSplashCount: Long = 0
    private var buttonClickCount: Long = 0
    fun isPrivacyOptionsRequired(): Boolean {
        return (consentInformation!!.getPrivacyOptionsRequirementStatus()
                == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED)
    }
    enum class ConsentDialogState {
        ACCEPTED,
        REJECTED,
        NOT_REQUIRED
    }

    interface ConsentDialogListener {
        fun onConsentFormDismissed(state: ConsentDialogState)
    }

    fun showDialogConsentMonkey(activity: Activity, listener: ConsentDialogListener) {
        if (PurchaseManagerInApp.getInstance().isPurchased()) {
            listener.onConsentFormDismissed(ConsentDialogState.NOT_REQUIRED)
            return
        }
        //            // Create a ConsentRequestParameters object.
        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation!!.requestConsentInfoUpdate(
            activity,
            getParams(activity),
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { loadAndShowError ->
                    if (loadAndShowError != null) {
                        // Consent gathering failed.
                        Log.w("TAG", "${loadAndShowError.errorCode}: ${loadAndShowError.message}")
                    }

                    if (isPrivacyOptionsRequired()) {
                        SharedPref.isShowPolicychange = true
                    }

                    // Consent has been gathered.
                    if (consentInformation!!.canRequestAds()) {
                        // mediation consent api EU
                        PangleMediationAdapter.setGDPRConsent(PAGConstant.PAGGDPRConsentType.PAG_GDPR_CONSENT_TYPE_CONSENT)
                        PangleMediationAdapter.setPAConsent(PAGConstant.PAGPAConsentType.PAG_PA_CONSENT_TYPE_CONSENT)
                        AppLovinPrivacySettings.setHasUserConsent(true, activity)
                        val mBridgeSDK = MBridgeSDKFactory.getMBridgeSDK()
                        mBridgeSDK.setConsentStatus(activity, MBridgeConstans.IS_SWITCH_ON)

                        // mediation consent api US
                        mBridgeSDK.setDoNotTrackStatus(activity, false)
                        listener.onConsentFormDismissed(ConsentDialogState.ACCEPTED)
                        YNMAirBridge.getInstance().logCustomEvent("consent", "accept")
                    } else {
                        AppLovinPrivacySettings.setHasUserConsent(false, activity)
                        listener.onConsentFormDismissed(ConsentDialogState.REJECTED)
                        YNMAirBridge.getInstance().logCustomEvent("consent", "reject")
                    }
                }
            }
        ) { requestConsentError ->
            // Consent gathering failed.
            Log.w("TAG", "${requestConsentError.errorCode}: ${requestConsentError.message}")
            YNMAirBridge.getInstance().logCustomEvent("consent", "error")
            listener.onConsentFormDismissed(ConsentDialogState.REJECTED)
        }

    }

    //    fun showConsentDialogSplash(
//        activity: Activity?,
//        listener: ConsentForm.OnConsentFormDismissedListener
//    ) {
//        RemoteConfigManager.instance!!.loadIsShowConsent(
//            activity!!,
//            object : RemoteConfigManager.BooleanCallback {
//                override fun onResult(value: Boolean) {
//                    if (value) {
//                        loadDialogForm(activity, null)
//                        requestConsentInfor(activity, {
//                            setRequiredConsentFirstOpen(activity)
//                            if (!consentRequired(activity)) {
//                                listener.onConsentFormDismissed(null)
//                                return@requestConsentInfor
//                            }
//                            if (canRequestAds(activity)) {
//                                listener.onConsentFormDismissed(null)
//                                return@requestConsentInfor
//                            }
//                            showDialogAtSplash(activity, listener)
//                        }) { formError: FormError? ->
//                            listener.onConsentFormDismissed(null)
//                            rejectSplashCount = 0
//                        }
//                    } else {
//                        listener.onConsentFormDismissed(null)
//                    }
//                }
//            })
//    }
//
//    private fun showDialogAtSplash(
//        activity: Activity?,
//        listener: ConsentForm.OnConsentFormDismissedListener
//    ) {
//        showDialog(activity!!, false) { formError: FormError? ->
//            // User rejected consent - just continue to next screen without billing dialog
//            listener.onConsentFormDismissed(formError)
//        }
//    }

//    fun showConsentDialogOnButtonClick(activity: Activity): Boolean {
//        return if (RemoteConfigManager.instance!!.isShowConsent) {
//            if (!consentRequired(activity)) {
//                return false
//            }
//            if (canRequestAds(activity)) {
//                false
//            } else showOnButtonClick(activity, null)
//        } else {
//            false
//        }
//    }

//    private fun showOnButtonClick(
//        activity: Activity,
//        dismissedListener: ConsentForm.OnConsentFormDismissedListener?
//    ): Boolean {
//        buttonClickCount++
//        return if (buttonClickCount == RemoteConfigManager.instance!!.limitFunctionClickCount()) {
//            // Show consent dialog without billing fallback
//            showDialog(activity, true) { formError: FormError? ->
//                buttonClickCount = 0
//                // Just navigate to permission activity after consent dialog
////                val intent = Intent(activity, PermissionActivity::class.java)
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                activity.startActivity(intent)
//            }
//            true
//        } else {
//            false
//        }
//    }
//
//    fun showConsentDialogWebView(activity: Activity) {
//        RemoteConfigManager.instance!!.loadIsShowConsent(
//            activity,
//            object : RemoteConfigManager.BooleanCallback {
//                override fun onResult(value: Boolean) {
//                    if (value) {
//                        showDialogAtWebView(activity)
//                    }
//                }
//            })
//    }

//    private fun showDialogAtWebView(activity: Activity) {
//        if (!consentRequired(activity)) {
//            return
//        }
//        loadDialogForm(activity, null)
//        val onDismiss = ConsentForm.OnConsentFormDismissedListener {
//            // Navigate to permission activity after consent dialog without billing
////            val intent = Intent(activity, PermissionActivity::class.java)
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////            activity.startActivity(intent)
//        }
//        showDialog(activity, true, onDismiss)
//    }

    companion object {
        private var INSTANCE: ConsentDialogManager? = null
        val instance: ConsentDialogManager?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = ConsentDialogManager()
                }
                return INSTANCE
            }
    }
}