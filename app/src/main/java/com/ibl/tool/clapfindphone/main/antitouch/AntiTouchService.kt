package com.ibl.tool.clapfindphone.main.antitouch

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.ibl.tool.clapfindphone.ACTION_FINISH_DETECT
import com.ibl.tool.clapfindphone.ACTION_NOTIFICATION_CLICKED_SERVICE
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.main.activity.AntiTouchActivity
import com.ibl.tool.clapfindphone.main.clap.ClassesApp
import com.ibl.tool.clapfindphone.main.clap.FeatureClapManager

class AntiTouchService : Service() {
    
    private lateinit var classesApp: ClassesApp
    private var screenReceiver: BroadcastReceiver? = null
    
    companion object {
        private const val CHANNEL_ID = "AntiTouchServiceChannel"
        private const val NOTIFICATION_ID = 2
        private const val TAG = "AntiTouchService"
    }
    
    override fun onCreate() {
        super.onCreate()
        classesApp = ClassesApp(this)
        createNotificationChannel()
        
        // Start foreground immediately in onCreate to avoid ForegroundServiceStartNotAllowedException
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                startForeground(
                    NOTIFICATION_ID, 
                    buildNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(NOTIFICATION_ID, buildNotification())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service", e)
            // If we can't start foreground, stop the service
            stopSelf()
            return
        }
        
        registerScreenReceiver()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started")
        
        // Notification already started in onCreate, just update if needed
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                startForeground(
                    NOTIFICATION_ID, 
                    buildNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(NOTIFICATION_ID, buildNotification())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onStartCommand", e)
        }
        
        return START_STICKY
    }
    
    private fun registerScreenReceiver() {
        screenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_ON -> {
                        Log.d(TAG, "Screen turned ON - Playing sound")
                        onScreenTurnedOn()
                    }
                    Intent.ACTION_USER_PRESENT -> {
                        Log.d(TAG, "User present (unlocked)")
                    }
                }
            }
        }
        
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(screenReceiver, filter)
    }
    
    private fun onScreenTurnedOn() {
        Log.d(TAG, "Playing sound/flash/vibration...")
        
        // Play sound, flash, vibrate when screen is turned on
        FeatureClapManager.getInstance(this).playAll()
        
        // Keep notification visible and don't stop service immediately
        // Service will be stopped when user clicks notification or manually deactivates
        Log.d(TAG, "Sound playing, notification remains visible")
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Anti Touch Protection",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Running anti-touch detection"
                setSound(null, null)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    @SuppressLint("LaunchActivityFromNotification")
    private fun buildNotification(): Notification {
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val intentNotificationAction = Intent(this, AntiTouchActivity::class.java).apply {
                action = ACTION_NOTIFICATION_CLICKED_SERVICE
            }
            
            val pendingIntentNotificationAction = PendingIntent.getActivity(
                this,
                0,
                intentNotificationAction,
                PendingIntent.FLAG_IMMUTABLE
            )
            
            val contentView = RemoteViews(this.packageName, R.layout.popup_notification)
            
            val notificationBuilder: NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Anti Touch Protection",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    lightColor = Color.GREEN
                    enableVibration(false)
                }
                notificationManager.createNotificationChannel(channel)
                notificationBuilder = NotificationCompat.Builder(this.applicationContext, CHANNEL_ID)
            } else {
                notificationBuilder = NotificationCompat.Builder(this.applicationContext)
            }
            
            contentView.setOnClickPendingIntent(
                R.id.notification_layout,
                pendingIntentNotificationAction
            )
            
            val smallIcon = if (Build.BRAND.equals("Google", ignoreCase = true)) {
                R.drawable.ic_icon_noti_pixel
            } else {
                R.drawable.ic_icon_app_small
            }
            
            notificationBuilder.setContent(contentView)
                .setCustomContentView(contentView)
                .setSmallIcon(smallIcon)
                .setCustomBigContentView(contentView)
                .setCustomHeadsUpContentView(contentView)
                .setContentIntent(pendingIntentNotificationAction)
                .setAutoCancel(true)
            
            val notification = notificationBuilder.build()
            try {
                notificationManager.notify(NOTIFICATION_ID, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return notification
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Return a default notification in case of failure
        return NotificationCompat.Builder(this.applicationContext, CHANNEL_ID)
            .setContentTitle("Anti Touch Protection")
            .setContentText("An error occurred while creating the notification.")
            .setSmallIcon(R.drawable.ic_icon_app_small)
            .build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
        
        try {
            screenReceiver?.let { unregisterReceiver(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Error unregistering receiver", e)
        }
        
        FeatureClapManager.getInstance(this).stopAll()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}

