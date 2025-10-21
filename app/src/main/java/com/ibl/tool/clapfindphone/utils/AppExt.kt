package com.ibl.tool.clapfindphone.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics


fun firebaseAnalyticsEvent(context: Context,eventName:String, key: String, data: String) {
        val bundle = Bundle()
        bundle.putString(key, data)
        firebaseAnalyticsEvent(eventName, bundle)
}

fun firebaseAnalyticsEvent(eventName:String, bundle: Bundle) {
        val firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(eventName, bundle)
        //log out debug log for firebase analytic event
        Log.d("Firebase-analytic-log", eventName + " - data: " + bundle.toString());
}