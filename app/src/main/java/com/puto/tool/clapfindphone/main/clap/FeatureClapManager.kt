package com.puto.tool.clapfindphone.main.clap

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.res.AssetFileDescriptor
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.puto.tool.clapfindphone.DEFAULT_SOUND_TYPE
import com.puto.tool.clapfindphone.IMPORT_SOUND_TYPE
import com.puto.tool.clapfindphone.utils.app.AppPreferences
import com.puto.tool.clapfindphone.utils.app.VibrateFlashThread
import java.io.IOException


class FeatureClapManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var isVibrating = false
    private var appPreferences = AppPreferences.instance
    private val handler = Handler(Looper.getMainLooper())
    private var runnable = Runnable {
        VibrateFlashThread.isCancellable = true
        stopSound()
        VibrateFlashThread.stopAll()
        callback?.invoke(true)
    }
    private val audioManager: AudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private var callback: ((Boolean) -> Unit)? = null

    private fun playSoundSaveGson() {
        val statusPlay = AppPreferences.instance.hasSound
        playSound(appPreferences.currentDuration.toLong(), statusPlay)
    }

    private fun setVolume() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, appPreferences.currentVolume, 0);
    }

    private fun playSound(duration: Long, statusPlay: Boolean) {
        if (!statusPlay) {
            return
        }
        if (mediaPlayer == null) mediaPlayer = MediaPlayer()
        setVolume()
        try {
            val currentSoundItem = appPreferences.currentSound
            when (currentSoundItem.type) {
                IMPORT_SOUND_TYPE -> {
                    mediaPlayer?.setDataSource(currentSoundItem.soundPath)
                }

                DEFAULT_SOUND_TYPE -> {
                    val assetFileDescriptor: AssetFileDescriptor = context.assets.openFd(
                        currentSoundItem.soundPath!!.substring(
                            currentSoundItem.soundPath!!.lastIndexOf(
                                "/"
                            ) + 1
                        )
                    )
                    mediaPlayer!!.setDataSource(
                        assetFileDescriptor.fileDescriptor,
                        assetFileDescriptor.startOffset,
                        assetFileDescriptor.length
                    )
                }
            }
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()

        } catch (e: IOException) {
            Log.d("error in opening audio file", e.toString())
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            Log.d("MediaPlayer has a status error", e.toString())
            e.printStackTrace()
        } catch (e: Exception) {
            Log.d("Other error", e.toString())
            e.printStackTrace()
        }
    }

    fun stopSound() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    fun flashSaveGson() {
        val statusFlash = AppPreferences.instance.hasFlash
        turnOnFlash(appPreferences.currentDuration.toLong(), statusFlash)
    }

//    private fun turnOnFlash(duration: Long, statusFlash: Boolean) {
//        if (!statusFlash) {
//            turnOffFlash()
//            return
//        }
//        val manager = context.getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
//        val cameraId: String?
//        try {
//            cameraId = manager.cameraIdList[0]
//
//            // Check flash of camera
//            val cameraCharacteristics = manager.getCameraCharacteristics(cameraId)
//            if (!cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!) {
//                handler.post {
//                    Toast.makeText(context, "Your Flash is error", Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//
//            manager.setTorchMode(cameraId, true)
//            VibrateFlashThread(context, AppPreferences(context).currentFlash, duration.toInt()).start()
//
//            handler.postDelayed({
//                try {
//                    manager.setTorchMode(cameraId, false)
//                } catch (e: CameraAccessException){
//                    e.printStackTrace()
//                }
//                manager.setTorchMode(cameraId, false)
//            }, duration)
//        } catch (e: CameraAccessException) {
//            throw RuntimeException(e)
//        }
//    }

    private fun turnOnFlash(duration: Long, statusFlash: Boolean) {
        if (!statusFlash) {
            turnOffFlash()
            return
        }
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraIds = manager.cameraIdList
            for (cameraId in cameraIds) {
                val cameraCharacteristics = manager.getCameraCharacteristics(cameraId)
                if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true) {
                    try {
                        manager.setTorchMode(cameraId, true)
                        VibrateFlashThread(
                            context,
                            AppPreferences(context).currentFlash,
                            duration.toInt()
                        ).start()
                        handler.postDelayed({
                            try {
                                manager.setTorchMode(cameraId, false)
                            } catch (e: CameraAccessException) {
                                Log.e("FeatureClapManager", "Error turning off flash", e)
                            } catch (e: Exception) {
                                Log.e("FeatureClapManager", "Error turning off flash", e)
                            }
                        }, duration)
                        return
                    } catch (e: CameraAccessException) {
                        // Camera is in use or unavailable, log and continue
                        Log.w("FeatureClapManager", "Camera in use or unavailable: ${e.message}")
                        // Try next camera if available
                        continue
                    }
                }
            }
            // No camera with flash available or all cameras are in use
            Log.w("FeatureClapManager", "No available camera with flash")
        } catch (e: CameraAccessException) {
            Log.e("FeatureClapManager", "Camera access error", e)
        } catch (e: Exception) {
            Log.e("FeatureClapManager", "Unexpected error in turnOnFlash", e)
        }
    }


    private fun turnOffFlash() {
        val manager = context.getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
        try {
            val cameraIds = manager.cameraIdList
            for (cameraId in cameraIds) {
                try {
                    manager.setTorchMode(cameraId, false)
                } catch (e: CameraAccessException) {
                    // Camera may be in use or unavailable, continue trying other cameras
                    Log.w("FeatureClapManager", "Error turning off flash for camera $cameraId: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("FeatureClapManager", "Error in turnOffFlash", e)
        }
    }

    private fun vibrationSaveGson() {
        val statusFlash = AppPreferences.instance.hasVibrate
        turnOnVibration(appPreferences.currentDuration.toLong(), statusFlash)
    }

    private fun turnOnVibration(duration: Long, statusVibration: Boolean) {
        if (!statusVibration) {
            turnOffVibration()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrateMode = AppPreferences(context).currentVibrate
            val vibrateFlashThread = VibrateFlashThread(context, vibrateMode, duration.toInt())
            vibrateFlashThread.start()
        }
    }

    private fun turnOffVibration() {
        VibrateFlashThread.stopAll()
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
        isVibrating = false
    }

    fun playAll() {
        handler.postDelayed(runnable, appPreferences.currentDuration.toLong())
        VibrateFlashThread.isCancellable = false
        vibrationSaveGson()
        flashSaveGson()
        playSoundSaveGson()
    }

    fun setCallback(callback: (Boolean) -> Unit) {
        this.callback = callback
    }

    fun stopAll() {
        try {
            handler.removeCallbacks(runnable)
            callback?.invoke(false)
            VibrateFlashThread.isCancellable = true
            turnOffFlash()
            turnOffVibration()
            stopSound()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: FeatureClapManager? = null

        fun getInstance(context: Context): FeatureClapManager {
            if (instance == null) {
                instance = FeatureClapManager(context)
            }
            return instance!!
        }
    }
}
