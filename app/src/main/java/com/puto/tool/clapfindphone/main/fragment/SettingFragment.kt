//package com.ibl.tool.clapfindphone.main.fragment
//
//import android.content.Intent
//import android.util.Log
//import android.view.View
//import com.ibl.tool.clapfindphone.MODE_FLASH_DEFAULT
//import com.ibl.tool.clapfindphone.MODE_FLASH_DISCO
//import com.ibl.tool.clapfindphone.MODE_FLASH_SOS
//import com.ibl.tool.clapfindphone.MODE_VIBRATE_DEFAULT
//import com.ibl.tool.clapfindphone.MODE_VIBRATE_HEART
//import com.ibl.tool.clapfindphone.MODE_VIBRATE_STRONG
//import com.ibl.tool.clapfindphone.MODE_VIBRATE_TICKTOCK
//import com.ibl.tool.clapfindphone.base.BaseFragment
//import com.ibl.tool.clapfindphone.data.preferences.SharedPrefs
//import com.ibl.tool.clapfindphone.databinding.FragmentSettingBinding
//import com.ibl.tool.clapfindphone.main.activity.PolicyWebViewActivity
//import com.ibl.tool.clapfindphone.utils.ActionUtils
//import com.ibl.tool.clapfindphone.utils.EventLogger
//import com.ibl.tool.clapfindphone.utils.LanguageUtils
//import com.ibl.tool.clapfindphone.utils.app.AppPreferences
//import com.ibl.tool.clapfindphone.utils.app.VibrateFlashThread
//import com.ibl.tool.clapfindphone.utils.hide
//
//class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
//    private lateinit var appPreferences: AppPreferences
//
//    override fun initView() {
//        appPreferences = AppPreferences(requireContext())
//        setUpSelection()
//        setUpRate()
//    }
//
//    private fun setUpSelection() {
//        when (appPreferences.currentFlash) {
//            MODE_FLASH_DEFAULT -> {
//                binding?.rbFlashDefault?.isChecked = true
//            }
//
//            MODE_FLASH_DISCO -> {
//                binding?.rbFlashDisco?.isChecked = true
//            }
//
//            MODE_FLASH_SOS -> {
//                binding?.rbFlashSos?.isChecked = true
//            }
//        }
//
//        when (appPreferences.currentVibrate) {
//            MODE_VIBRATE_DEFAULT -> {
//                binding?.rbVibrateDefault?.isChecked = true
//            }
//
//            MODE_VIBRATE_STRONG -> {
//                binding?.rbVibrateStrong?.isChecked = true
//            }
//
//            MODE_VIBRATE_HEART -> {
//                binding?.rbVibrateHeart?.isChecked = true
//            }
//
//            MODE_VIBRATE_TICKTOCK -> {
//                binding?.rbVibrateTicktock?.isChecked = true
//            }
//        }
//
//        binding?.sbSound?.isChecked = appPreferences.hasSound
//        if (appPreferences.hasFlash) {
//            binding?.sbFlash?.isChecked = true
//            unlockFlash()
//        } else {
//            binding?.sbFlash?.isChecked = false
//            lockFlash()
//        }
//        if (appPreferences.hasVibrate) {
//            binding?.sbVibrate?.isChecked = true
//            unlockVibrate()
//        } else {
//            binding?.sbVibrate?.isChecked = false
//            lockVibrate()
//        }
//    }
//
//    override fun addEvent() {
//        binding?.apply {
//            rbFlashDefault.setOnClickListener {
//                logEvent("click_set_flash_default")
//                runVibrateFlashThread(MODE_FLASH_DEFAULT)
//            }
//            rbFlashDisco.setOnClickListener {
//                logEvent("click_set_flash_disco")
//                runVibrateFlashThread(MODE_FLASH_DISCO)
//            }
//            rbFlashSos.setOnClickListener {
//                logEvent("click_set_flash_SOS")
//                runVibrateFlashThread(MODE_FLASH_SOS)
//
//            }
//            rbVibrateDefault.setOnClickListener {
//                logEvent("click_set_vibrate_default")
//                runVibrateFlashThread(MODE_VIBRATE_DEFAULT)
//
//            }
//            rbVibrateStrong.setOnClickListener {
//                logEvent("click_set_vibrate_strong")
//                runVibrateFlashThread(MODE_VIBRATE_STRONG)
//            }
//            rbVibrateHeart.setOnClickListener {
//                logEvent("click_set_vibrate_heartbeat")
//                runVibrateFlashThread(MODE_VIBRATE_HEART)
//            }
//            rbVibrateTicktock.setOnClickListener {
//                logEvent("click_set_vibrate_ticktock")
//                runVibrateFlashThread(MODE_VIBRATE_TICKTOCK)
//
//            }
//            llFlashController.setOnClickListener {
//                sbFlash.isChecked = !sbFlash.isChecked
//            }
//            sbFlash.setOnCheckedChangeListener { _, isChecked ->
//                if (!isChecked) {
//                    logEvent("click_set_flash_off")
//                    lockFlash()
//                } else {
//                    logEvent("click_set_flash_on")
//                    unlockFlash()
//                }
//            }
//
//            llVibrateController.setOnClickListener {
//                sbVibrate.isChecked = !sbVibrate.isChecked
//            }
//            sbVibrate.setOnCheckedChangeListener { _, isChecked ->
//                if (!isChecked) {
//                    logEvent("click_set_vibrate_off")
//                    lockVibrate()
//                } else {
//                    logEvent("click_set_vibrate_on")
//                    unlockVibrate()
//                }
//            }
//            sbSound.setOnCheckedChangeListener { _, isChecked ->
//                if (!isChecked) {
//                    logEvent("click_set_sound_off")
//                } else {
//                    logEvent("click_set_sound_on")
//                }
//            }
//
//            llSoundController.setOnClickListener {
//                sbSound.isChecked = !sbVibrate.isChecked
//            }
//            vLockFlash.setOnClickListener { Log.e("android_log_error", "it is disabled") }
//            vLockVibrate.setOnClickListener { Log.e("android_log_error", "it is disabled") }
//
//            btnLanguage.setOnClickListener {
//                EventLogger.getInstance()?.logEvent("click_set_language")
//            }
//            btnRate.setOnClickListener {
//                EventLogger.getInstance()?.logEvent("click_set_rate")
//                ActionUtils.showRateDialog(requireActivity(), false, callback = {
//                    if (it) hideRate()
//                })
//            }
//            btnShare.setOnClickListener {
//                EventLogger.getInstance()?.logEvent("click_set_share")
//                ActionUtils.shareApp(requireActivity())
//            }
//            btnFeedback.setOnClickListener {
//                ActionUtils.sendFeedback(requireActivity())
//            }
//            btnPrivacy.setOnClickListener {
//                PolicyWebViewActivity.start(requireActivity())
//            }
//        }
//    }
//
//    private fun runVibrateFlashThread(mode: Int) {
//        if (VibrateFlashThread.isCancellable) {
//            VibrateFlashThread(
//                requireContext(),
//                mode
//            ).start()
//        } else {
//            when (mode) {
//                MODE_FLASH_DEFAULT, MODE_FLASH_DISCO, MODE_FLASH_SOS -> {
//                    appPreferences.currentFlash = mode
//                }
//
//                MODE_VIBRATE_DEFAULT, MODE_VIBRATE_STRONG, MODE_VIBRATE_HEART, MODE_VIBRATE_TICKTOCK -> {
//                    appPreferences.currentVibrate = mode
//                }
//            }
//        }
//    }
//
//    private fun unlockFlash() {
//        binding?.rgFlash?.alpha = 1F
//        binding?.vLockFlash?.visibility = View.GONE
//    }
//
//    private fun lockFlash() {
//        if (VibrateFlashThread.isCancellable) {
//            VibrateFlashThread.stopFlash()
//        }
//        binding?.rgFlash?.alpha = 0.3F
//        binding?.vLockFlash?.visibility = View.VISIBLE
//    }
//
//    private fun unlockVibrate() {
//        binding?.rgVibrate?.alpha = 1F
//        binding?.vLockVibrate?.visibility = View.GONE
//    }
//
//    private fun lockVibrate() {
//        if (VibrateFlashThread.isCancellable) {
//            VibrateFlashThread.stopVibrate()
//        }
//        binding?.rgVibrate?.alpha = 0.3F
//        binding?.vLockVibrate?.visibility = View.VISIBLE
//    }
//
//
//    public fun hideRate() {
//        binding?.btnRate?.hide()
//    }
//
//    private fun setUpRate() {
//        if (SharedPrefs.isRated(requireActivity())) {
//            hideRate()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        binding?.apply {
//            appPreferences.hasSound = sbSound.isChecked
//            appPreferences.hasFlash = sbFlash.isChecked
//            appPreferences.hasVibrate = sbVibrate.isChecked
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        binding?.ivFlag?.setImageResource(LanguageUtils.listCountryDefault[appPreferences.currentIndexLanguage].imageFlag)
//    }
//}