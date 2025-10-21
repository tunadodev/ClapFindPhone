package com.ibl.tool.clapfindphone.main.activity

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ads.nomyek_admob.utils.AdsNativeMultiPreload
import com.bumptech.glide.Glide
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.KEY_SOUND_ITEM_DATA
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.data.model.SoundItem
import com.ibl.tool.clapfindphone.databinding.ActivitySoundSettingsBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity
import com.ibl.tool.clapfindphone.utils.AppExtension
import com.ibl.tool.clapfindphone.utils.Utils
import com.ibl.tool.clapfindphone.utils.app.AppPreferences
import com.ibl.tool.clapfindphone.utils.app.MediaPlayerAppUtil

class SoundSettingsActivity : BaseObdActivity<ActivitySoundSettingsBinding>() {

    private lateinit var currentSoundItem: SoundItem
    private var currentDuration = 30
    private var currentVolume = 70
    private var hasVibration = true
    private var hasFlashlight = true
    private var isPlaying = false
    private lateinit var audioManager: AudioManager
    private val appPreferences = AppPreferences.instance

    companion object {
        fun start(context: Context, soundItem: SoundItem) {
            Utils.showInterPreload(context, "sound_settings_screen", object : Runnable {
                override fun run() {
                    val intent = Intent(context, SoundSettingsActivity::class.java)
                    intent.putExtra(KEY_SOUND_ITEM_DATA, soundItem)
                    context.startActivity(intent)
                }
            })
        }
    }

    override fun getLayoutActivity(): Int {
        return R.layout.activity_sound_settings
    }

    override fun initViews() {
        nameView = "sound_settings_screen"
        
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentSoundItem = intent.getSerializableExtra(KEY_SOUND_ITEM_DATA) as SoundItem
        
        setupUI()
        loadCurrentSettings()
        addEvent()
        val listAdId = listOf(
            AdsNativeMultiPreload.AdIdModel().apply {
                adId = BuildConfig._501_alertsetting_native
                adName = "alert_setting"
            }
        )
        showNative(listAdId, "alert_setting")
    }

    private fun setupUI() {
        // Set sound avatar and name
        Glide.with(this).load(currentSoundItem.avatar).into(viewBinding.ivSoundAvatar)
        viewBinding.tvSoundName.text = currentSoundItem.name
        
        // Setup seekbar
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        viewBinding.seekBarVolume.setMax(maxVolume)
        
        // Set initial duration selection
        updateDurationUI(currentDuration)
    }

    private fun loadCurrentSettings() {
        // Load from preferences if this sound is already selected
        if (appPreferences.currentSound.soundPath == currentSoundItem.soundPath) {
            currentDuration = appPreferences.currentDuration / 1000
            currentVolume = appPreferences.currentVolume
            hasVibration = appPreferences.hasVibrate
            hasFlashlight = appPreferences.hasFlash
        } else {
            // Default values
            currentVolume = (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 0.7).toInt()
        }
        
        viewBinding.seekBarVolume.presetProgress(currentVolume)
        viewBinding.switchVibration.isChecked = hasVibration
        viewBinding.switchFlashlight.isChecked = hasFlashlight
        updateDurationUI(currentDuration)
    }

    private fun addEvent() {
        viewBinding.btnClose.setOnClickListener {
            elementClickEvent("sound_settings_close_click", "button")
            Utils.showInterPreload(this, nameView, object : Runnable {
                override fun run() {
                    finish()
                }
            })

        }

        viewBinding.btnSave.setOnClickListener {
            elementClickEvent("sound_settings_save_click", "button")
            saveSettings()
        }

        viewBinding.btnPlay.setOnClickListener {
            elementClickEvent("sound_settings_play_click", "button")
            togglePlayPause()
        }

        // Duration buttons
        viewBinding.tvDuration5s.setOnClickListener {
            elementClickEvent("sound_settings_duration_5s_click", "button")
            updateDuration(5)
        }
        
        viewBinding.tvDuration15s.setOnClickListener {
            elementClickEvent("sound_settings_duration_15s_click", "button")
            updateDuration(15)
        }

        viewBinding.tvDuration30s.setOnClickListener {
            elementClickEvent("sound_settings_duration_30s_click", "button")
            updateDuration(30)
        }

        viewBinding.tvDuration45s.setOnClickListener {
            elementClickEvent("sound_settings_duration_45s_click", "button")
            updateDuration(45)
        }

        viewBinding.tvDuration1m.setOnClickListener {
            elementClickEvent("sound_settings_duration_1m_click", "button")
            updateDuration(60)
        }

        // Volume seekbar
        viewBinding.seekBarVolume.setSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    currentVolume = progress
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                }
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        // Switches
        viewBinding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            elementClickEvent("sound_settings_vibration_${if (isChecked) "on" else "off"}", "switch")
            hasVibration = isChecked
        }

        viewBinding.switchFlashlight.setOnCheckedChangeListener { _, isChecked ->
            elementClickEvent("sound_settings_flashlight_${if (isChecked) "on" else "off"}", "switch")
            hasFlashlight = isChecked
        }
    }

    private fun togglePlayPause() {
        if (!isPlaying) {
            // Play
            MediaPlayerAppUtil.playAudio(this, currentSoundItem) {
                // On complete
                runOnUiThread {
                    isPlaying = false
                    viewBinding.tvPlayText.text = getString(R.string.play)
                    viewBinding.ivPlayIcon.setImageResource(R.drawable.ic_pause)
                }
            }
            isPlaying = true
            viewBinding.tvPlayText.text = getString(R.string.pause)
            viewBinding.ivPlayIcon.setImageResource(R.drawable.ic_resume)
        } else {
            // Pause
            MediaPlayerAppUtil.stopAudio()
            isPlaying = false
            viewBinding.tvPlayText.text = getString(R.string.play)
            viewBinding.ivPlayIcon.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun updateDuration(duration: Int) {
        if (currentDuration != duration) {
            currentDuration = duration
            updateDurationUI(duration)
        }
    }

    private fun updateDurationUI(duration: Int) {
        viewBinding.tvDuration5s.isSelected = duration == 5
        viewBinding.tvDuration15s.isSelected = duration == 15
        viewBinding.tvDuration30s.isSelected = duration == 30
        viewBinding.tvDuration45s.isSelected = duration == 45
        viewBinding.tvDuration1m.isSelected = duration == 60
    }

    private fun saveSettings() {
        // Save to preferences
        appPreferences.currentSound = currentSoundItem
        appPreferences.currentDuration = currentDuration * 1000
        appPreferences.currentVolume = currentVolume
        appPreferences.hasVibrate = hasVibration
        appPreferences.hasFlash = hasFlashlight
        
        Toast.makeText(this, getString(R.string.save_successfully), Toast.LENGTH_SHORT).show()
        
        // Return result
        setResult(RESULT_OK)
        Utils.showInterPreload(this, nameView, object : Runnable {
            override fun run() {
                finish()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayerAppUtil.stopAudio()
    }
}

