//package com.ibl.tool.clapfindphone.utils;
//
//import android.Manifest.permission.READ_EXTERNAL_STORAGE
//import android.app.Activity
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.content.res.Configuration
//import android.content.res.Resources
//import android.graphics.*
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.ShapeDrawable
//import android.graphics.drawable.shapes.RoundRectShape
//import android.os.Build
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.provider.Settings
//import android.util.Log
//import android.util.TypedValue
//import android.view.View
//import android.view.ViewOutlineProvider
//import android.widget.Toast
//import androidx.appcompat.widget.AppCompatImageView
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.ibl.tool.clapfindphone.main.activity.HomeActivity
//import java.util.*
//
//object AppExtension {
//
//    @JvmStatic
//    fun goHomeActivity(context: Context, bundle: Bundle?) {
//        showActivity(context, HomeActivity::class.java, bundle, 0, false)
//    }
//
//    @JvmStatic
//    @JvmOverloads
//    fun showActivity(context: Context, activity: Class<*>, bundle: Bundle?, delay: Int = 0, isShowInter: Boolean = true) {
//        if (isShowInter) {
//            Utils.showInterPreload(context, activity.name, object : Runnable {
//                override fun run() {
//                    if (delay > 0) {
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            val intent = Intent(context, activity)
//                            intent.putExtras(bundle ?: Bundle())
//                            context.startActivity(intent)
//                        }, delay.toLong()) // Delay in milliseconds
//                    } else {
//                        val intent = Intent(context, activity)
//                        intent.putExtras(bundle ?: Bundle())
//                        context.startActivity(intent)
//                    }
//                }
//            })
//            return;
//        }
//        if (delay > 0) {
//            Handler(Looper.getMainLooper()).postDelayed({
//                val intent = Intent(context, activity)
//                intent.putExtras(bundle ?: Bundle())
//                context.startActivity(intent)
//            }, delay.toLong()) // Delay in milliseconds
//        } else {
//            val intent = Intent(context, activity)
//            intent.putExtras(bundle ?: Bundle())
//            context.startActivity(intent)
//        }
//    }
//
//    @JvmStatic
//    fun getAlphaPercentage(color: Int): Int {
//        val alpha = Color.alpha(color)
//        return ((alpha / 255.0) * 100).toInt()
//    }
//
//    // Helper method to convert dp to pixels
//    @JvmStatic
//    fun dpToPx(context: Context, dp: Int): Int {
//        val displayMetrics = context.resources.displayMetrics
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics).toInt()
//    }
//
//    @JvmStatic
//    fun openAccessibilitySettingsWithHighlight(context: Context, path: String) {
//        var intent = Intent("com.samsung.accessibility.installed_service") // Samsung specific intent
//        if (intent.resolveActivity(context.packageManager) == null) {
//            intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS) // Fallback to general Accessibility Settings
//        }
//
//        val EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key"
//        val EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args"
//
//        val bundle = Bundle()
//        val serviceComponentName = "${context.packageName}/$path" // Your service component name
//        bundle.putString(EXTRA_FRAGMENT_ARG_KEY, serviceComponentName)
//        intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, serviceComponentName)
//        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle)
//        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
//
//        try {
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            // Handle exception (e.g., log it, fallback to regular settings intent)
//            e.printStackTrace()
//            val fallbackIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
//            context.startActivity(fallbackIntent)
//        }
//    }
//
//    @JvmStatic
//    fun openNotificationListenerSettingsWithHighlight(context: Context, notificationListenerServiceClass: Class<*>) {
//        var intent: Intent? = null
//
//        // Attempt to use a direct intent to the listener settings (may not be universally supported)
//        intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
//
//        // Fallback if the direct intent doesn't work or for broader compatibility, just open the general settings.
//        if (intent == null || intent.resolveActivity(context.packageManager) == null) {
//            intent = Intent(Settings.ACTION_SETTINGS) // General settings page as fallback
//        }
//
//        // Attempt to pass component name as fragment argument (similar to Accessibility, might not work reliably)
//        val EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key"
//        val EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args"
//
//        val bundle = Bundle()
//        val serviceComponentName = ComponentName(context.packageName, notificationListenerServiceClass.name)
//        bundle.putString(EXTRA_FRAGMENT_ARG_KEY, serviceComponentName.flattenToString()) // Use flattenToString for ComponentName
//        intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, serviceComponentName.flattenToString())
//        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle)
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
//
//        try {
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            // Handle exception (e.g., log it, fallback to regular settings intent)
//            e.printStackTrace()
//            val fallbackIntent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS) // Or Settings.ACTION_SETTINGS
//            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
//            context.startActivity(fallbackIntent)
//        }
//    }
//
//    @JvmStatic
//    fun createCheckerboardBitmap(context: Context): Drawable {
//        val squareSizePx = dpToPx(context, 8)
//        val bitmapSizePx = squareSizePx * 2
//        val cornerRadiusPx = dpToPx(context, 100).toFloat() // Radius for corners - MUST BE THE SAME RADIUS AS GRADIENT
//
//        val checkerboardBitmap = Bitmap.createBitmap(bitmapSizePx, bitmapSizePx, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(checkerboardBitmap)
//        val paint = Paint()
//
//        val colorLight = Color.parseColor("#E0E0E0")
//        val colorDark = Color.parseColor("#B0B0B0")
//
//        // Fill background (optional, can be transparent)
//        paint.color = Color.WHITE // Or Color.TRANSPARENT
//        canvas.drawRect(0f, 0f, bitmapSizePx.toFloat(), bitmapSizePx.toFloat(), paint)
//
//        // Draw checkerboard squares
//        paint.color = colorLight
//        canvas.drawRect(0f, 0f, squareSizePx.toFloat(), squareSizePx.toFloat(), paint)
//        canvas.drawRect(squareSizePx.toFloat(), squareSizePx.toFloat(), bitmapSizePx.toFloat(), bitmapSizePx.toFloat(), paint)
//
//        paint.color = colorDark
//        canvas.drawRect(squareSizePx.toFloat(), 0f, bitmapSizePx.toFloat(), squareSizePx.toFloat(), paint)
//        canvas.drawRect(0f, squareSizePx.toFloat(), squareSizePx.toFloat(), bitmapSizePx.toFloat(), paint)
//
//        val shader = BitmapShader(checkerboardBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
//        paint.shader = shader
//
//        val roundedRectShape = RoundRectShape(
//            floatArrayOf(cornerRadiusPx, cornerRadiusPx, cornerRadiusPx, cornerRadiusPx, cornerRadiusPx, cornerRadiusPx, cornerRadiusPx, cornerRadiusPx),
//            null,
//            null
//        )
//
//        val roundedCheckerboard = ShapeDrawable(roundedRectShape)
//        roundedCheckerboard.paint.shader = shader // Apply the shader to the ShapeDrawable's paint
//
//        return roundedCheckerboard
//    }
//
//    @JvmStatic
//    fun jumpToCastSetting(activity: Activity) {
//        val intent = Intent(Settings.ACTION_CAST_SETTINGS)
//        if (intent.resolveActivity(activity.packageManager) != null) {
//            activity.startActivity(intent)
//        } else {
//            // Handle the case where the Cast settings activity is not found
//            Toast.makeText(activity, "Cast settings not found on this device", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun interface PermissionCallback {
//        fun onPermissionGranted()
//    }
//
//    @JvmStatic
//    fun checkAndRequestExternalPermission(activity: Activity, callback: PermissionCallback, permission: String, code: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, arrayOf(permission), code)
//            } else {
//                callback.onPermissionGranted()
//            }
//        } else {
//            if (ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, arrayOf(READ_EXTERNAL_STORAGE), code)
//            } else {
//                callback.onPermissionGranted()
//            }
//        }
//    }
//
//    @JvmStatic
//    fun getLocalizedText(context: Context, localeId: String, stringId: Int): String {
//        return try {
//            val config = Configuration(context.resources.configuration)
//            val locale = Locale(localeId)
//            config.locale = locale
//            val configContext = context.createConfigurationContext(config) // Corrected line
//            val resources = configContext.resources // Get resources from the config context
//            resources.getString(stringId)
//        } catch (e: Resources.NotFoundException) {
//            Log.e("LocalizationError", "String resource not found for locale: $localeId, ID: $stringId", e)
//            context.getString(stringId) // Fallback to default locale
//        } catch (e: IllegalArgumentException) {
//            Log.e("LocalizationError", "Invalid locale ID: $localeId", e)
//            context.getString(stringId) // Fallback to default locale
//        }
//    }
//
//    /**
//     * Sets a rounded corner radius to an AppCompatImageView, clipping the image itself.
//     * This method uses ViewOutlineProvider for efficient clipping and works on API level 21+.
//     *
//     * @param imageView The AppCompatImageView to modify.
//     * @param radius    The radius in dp (density-independent pixels).
//     */
//    // Requires API 21+
//    @JvmStatic
//    fun setCornerRadius(imageView: AppCompatImageView?, radius: Double) {
//        if (imageView == null) {
//            return
//        }
//
//        val context = imageView.context
//
//        // Convert dp to pixels.
//        val radiusPx = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            radius.toFloat(),
//            context.resources.displayMetrics
//        )
//
//        // Set the ViewOutlineProvider to clip the image view.
//        imageView.outlineProvider = object : ViewOutlineProvider() {
//            override fun getOutline(view: View, outline: Outline) {
//                outline.setRoundRect(0, 0, view.width, view.height, radiusPx)
//            }
//        }
//
//        imageView.clipToOutline = true // Enable clipping to the outline.
//    }
//}
