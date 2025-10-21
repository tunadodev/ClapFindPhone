package com.ibl.tool.clapfindphone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {

    private static SharedPreferences mSharePref;
    private static Context mContext;
    public static boolean isShowPolicychange = false;
    public static void init(Context context) {
        mContext = context;
        if (mSharePref == null)
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static String readString(String str, String str2) {
        return mSharePref.getString(str, str2);
    }

    public static void saveString(String str, String str2) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static int readInteger(String str, int number) {
        return mSharePref.getInt(str, number);
    }

    public static void saveInteger(String str, int number) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putInt(str, number);
        edit.apply();
    }

    public static boolean readBoolean(String str, Boolean bool) {
        return mSharePref.getBoolean(str, bool);
    }

    public static void saveBoolean(String str, Boolean bool) {
        SharedPreferences.Editor edit = mSharePref.edit();
        edit.putBoolean(str, bool);
        edit.apply();
    }
}
