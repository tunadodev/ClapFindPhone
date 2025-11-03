//package com.ibl.tool.clapfindphone.utils;
//
//import static com.ibl.tool.clapfindphone.utils.AppExtension.showActivity;
//
//import com.ads.nomyek_admob.ads_components.YNMAds;
//import com.ads.nomyek_admob.ads_components.YNMAdsCallbacks;
//import com.ads.nomyek_admob.event.YNMAirBridge;
//import com.ads.nomyek_admob.utils.AdsInterPreload;
//import com.google.gson.Gson;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.res.AssetManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.SystemClock;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.Window;
//import android.view.animation.AlphaAnimation;
//
//import com.google.common.reflect.TypeToken;
//import com.ibl.tool.clapfindphone.BuildConfig;
//import com.ibl.tool.clapfindphone.R;
//import com.ibl.tool.clapfindphone.app.AppConstants;
//import com.ibl.tool.clapfindphone.app.ObdConstants;
//import com.ibl.tool.clapfindphone.onboard_flow.remote_config.RemoteConfigManager;
//import com.ibl.tool.clapfindphone.utils.purchase.PurchaseManagerInApp;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Locale;
//import java.util.Set;
//
//
//public class  Utils {
//    public static boolean firstOpenApp = true;
//
//    public static int dialogCount = 1;
//
//    public static Dialog dialog;
//
//    public static String currentTheme = "";
//
//    public static void pdialog(Activity context) {
//        try {
//            if (dialog != null)
//                if (dialog.isShowing())
//                    dialog.dismiss();
//            dialog = new Dialog(context);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.setContentView(R.layout.custom_progress_api);
//            dialog.setCancelable(false);
//            dialog.show();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (Error e2) {
//            e2.toString();
//        } catch (Exception e) {
//            e.toString();
//        }
//    }
//
//
//    public static void pdialog_dismiss() {
//        try {
//            if (dialog != null && dialog.isShowing())
//                dialog.dismiss();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (Error e2) {
//            e2.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public static AlphaAnimation clickAnimation() {
//        return new AlphaAnimation(1F, 0.4F); // Change "0.4F" as per your recruitment.
//    }
//
//    public static void saveStringList(Context context, String elementToAdd, String key) {
//        // Get the SharedPreferences instance
//        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        // Check if the set is empty (no existing data)
//        Set<String> existingStringSet = new HashSet<>(sharedPreferences.getStringSet(key, new HashSet<>()));
//
//        // If element is not a duplicate, add it to a new list prepended with the element
//        List<String> newList = new ArrayList<>();
//        if (!existingStringSet.contains(elementToAdd)) {
//            newList.add(elementToAdd);
//        }
//
//        // Add existing elements to the new list (ensuring order)
//        newList.addAll(existingStringSet);
//
//        // Convert the new list back to a set (needed for SharedPreferences)
//        Set<String> modifiedSet = new HashSet<>(newList);
//
//        // Save the modified set to SharedPreferences
//        editor.putStringSet(key, modifiedSet);
//        editor.apply(); // Use apply instead of commit for asynchronous saving
//    }
//
//    public static List<String> getStringList(Context context, String key) {
//        // Get the SharedPreferences instance
//        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
//
//        // Retrieve the set of strings (or empty set if none exists)
//        Set<String> stringSet = sharedPreferences.getStringSet(key, new HashSet<>());
//
//        // Convert the set back to a list
//        return new ArrayList<>(stringSet);
//    }
//
//    public static void performTouch(View view) {
//        long downTime = SystemClock.uptimeMillis();
//        long eventTime = SystemClock.uptimeMillis() + 100;
//        int metaState = 0;
//
//        // Default coordinates for the touch event
//        float startX = view.getX();
//        float startY = view.getY();
//        float endX = startX + 50; // Move 50 pixels to the right
//        float endY = startY + 50; // Move 50 pixels down
//
//        // Simulate ACTION_DOWN event
//        MotionEvent motionEvent = MotionEvent.obtain(
//                downTime,
//                eventTime,
//                MotionEvent.ACTION_DOWN,
//                startX,
//                startY,
//                metaState
//        );
//        view.dispatchTouchEvent(motionEvent);
//
//        // Simulate ACTION_MOVE event
//        motionEvent = MotionEvent.obtain(
//                downTime,
//                eventTime + 100,
//                MotionEvent.ACTION_MOVE,
//                endX,
//                endY,
//                metaState
//        );
//        view.dispatchTouchEvent(motionEvent);
//
//        // Simulate ACTION_UP event
//        motionEvent = MotionEvent.obtain(
//                downTime,
//                eventTime + 200,
//                MotionEvent.ACTION_UP,
//                endX,
//                endY,
//                metaState
//        );
//        view.dispatchTouchEvent(motionEvent);
//    }
//
//    private static String getCurrentDate() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        return dateFormat.format(new Date());
//    }
//
//    // Check if the last login date is today
//    public static boolean isLastShowToday() {
//        String lastLoginDate = SharedPref.readString(AppConstants.LAST_DAY_LOGIN, "");
//        SharedPref.saveString(AppConstants.LAST_DAY_LOGIN, getCurrentDate());
//        if (lastLoginDate == "") {
//            // No login date saved, meaning user hasn't logged in yet
//            return false;
//        }
//
//        // Get current date in "yyyy-MM-dd" format
//        String currentDate = getCurrentDate();
//        // Compare the saved last login date with the current date
//        return lastLoginDate.equals(currentDate);
//    }
//
//    // Check if today is the first day the user logged in
//    public static boolean isFirstLoginToday() {
//        String firstLoginDate = SharedPref.readString(AppConstants.FIRST_DAY_LOGIN, "");
//        if (firstLoginDate == "") {
//            SharedPref.saveString(AppConstants.FIRST_DAY_LOGIN, getCurrentDate());
//            return true;
//        }
//
//        // Get current date in "yyyy-MM-dd" format
//        String currentDate = getCurrentDate();
//
//        // Compare the saved first login date with the current date
//        return firstLoginDate.equals(currentDate);
//    }
//
//    public static boolean isFirstOpenUser() {
//        return SharedPref.readBoolean(AppConstants.FIRST_OPEN, true);
//    }
//    public static void setFirstOpenApp(boolean isFirstOpen) {
//        SharedPref.saveBoolean(AppConstants.FIRST_OPEN, isFirstOpen);
//    }
//    public static void setFinishObd(boolean isFinish) {
//        SharedPref.saveBoolean(AppConstants.FINISH_OBD, isFinish);
//    }
//    public static boolean isFinishObd() {
//        return SharedPref.readBoolean(AppConstants.FINISH_OBD, false);
//    }
//
//    public static String getUserState() {
//        return isFirstOpenUser() ? "first_user" : isFinishObd() ? "return" : "return_obd";
//    }
//    public static boolean isDisableObdAd() {
//        if (PurchaseManagerInApp.getInstance().isPurchased() || RemoteConfigManager.getInstance().getDisableObdAds() || RemoteConfigManager.getInstance().getDisableAllAds()) {
//            return true;
//        }
//        return false;
//    }
//    public static boolean isDisableAllAd() {
//        if (PurchaseManagerInApp.getInstance().isPurchased() || RemoteConfigManager.getInstance().getDisableObdAds() || RemoteConfigManager.getInstance().getDisableAllAds()) {
//            return true;
//        }
//        return false;
//    }
//
//    public static void preloadInterClick(Context context, String nameView) {
//        if (Utils.isDisableAllAd()) {
//            return;
//        }
//        boolean isBlock = true;
//        int delta = YNMAds.getInstance().getAdConfig().getCurrentInterIndex() - YNMAds.getInstance().getAdConfig().getInterStartIndex();
//        if (delta >= 0) {
//            if (delta == 0 || YNMAds.blockCount >= YNMAds.getInstance().getAdConfig().getInterDeltaIndex()) {
//                isBlock = false;
//            }
//        }
//        if (!YNMAds.isGoHome) isBlock = false;
//        if (!isBlock) {
//            AdsInterPreload.preloadInterAds(context,new YNMAirBridge.AppData(nameView, "inter"), BuildConfig._402_click_inter, ObdConstants.CLICK_INTER, 20000);
//        }
//    }
//
//    public static void showInterPreload(Context context, String nameView, Runnable callback) {
//        if (Utils.isDisableAllAd()) {
//            callback.run();
//            return;
//        }
//        AdsInterPreload.showPreloadInterAds(context, ObdConstants.CLICK_INTER, BuildConfig._402_click_inter, 10000, new YNMAdsCallbacks() {
//            @Override
//            public void onNextAction(boolean showed) {
//                super.onNextAction(showed);
////                AdsInterPreload.preloadInterAds(context,new YNMAirBridge.AppData(nameView, "inter"), BuildConfig._403_click_inter, ObdConstants.CLICK_INTER, 10000);
//                callback.run();
//            }
//
//            @Override
//            public void onInterstitialShow() {
//                super.onInterstitialShow();
//            }
//        });
//        preloadInterClick(context, nameView);
//    }
//
//    public static void setUpSplashApp() {
//
//    }
//}
