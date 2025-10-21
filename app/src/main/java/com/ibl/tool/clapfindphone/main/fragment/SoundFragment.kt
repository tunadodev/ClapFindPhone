//package com.ibl.tool.clapfindphone.main.fragment
//
//import android.content.Intent
//import android.util.Log
//import androidx.recyclerview.widget.GridLayoutManager
//import com.ibl.tool.clapfindphone.ADAPTER_ADS_TYPE
//import com.ibl.tool.clapfindphone.ADAPTER_ITEM_TYPE
//import com.ibl.tool.clapfindphone.KEY_SOUND
//import com.ibl.tool.clapfindphone.KEY_SOUND_ITEM_DATA
//import com.ibl.tool.clapfindphone.base.BaseFragment
//import com.ibl.tool.clapfindphone.data.model.SoundItem
//import com.ibl.tool.clapfindphone.data.repo.AppRepository
//import com.ibl.tool.clapfindphone.databinding.FragmentSoundBinding
//import com.ibl.tool.clapfindphone.main.activity.PlaySoundActivity
//import com.ibl.tool.clapfindphone.main.adapter.SoundAdapter
//
//class SoundFragment : BaseFragment<FragmentSoundBinding>(FragmentSoundBinding::inflate) {
//    private lateinit var soundAdapter: SoundAdapter
//    private var isFirstLoad = true;
//
//    override fun initView() {
//        setupList()
//    }
//
//    private fun setupList() {
//        soundAdapter = SoundAdapter(AppRepository.getAllSound(requireContext()), requireActivity(), isFirstLoad)
//        val gridLayoutManager = GridLayoutManager(context, 3)
//        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when (soundAdapter.getItemViewType(position)) {
//                    ADAPTER_ADS_TYPE -> 3
//                    ADAPTER_ITEM_TYPE -> 1
//                    else -> -1
//                }
//            }
//        }
////        FIX ME
////        soundAdapter.mCallback = OnActionCallback { key, data ->
////            if (key == KEY_SOUND) {
////                val soundItem = data[0] as SoundItem
////                logEvent("click_detail_play_" + soundItem.name?.replace(" ", "_")?.lowercase())
////                val intent = Intent(activity, PlaySoundActivity::class.java)
////                intent.putExtra(KEY_SOUND_ITEM_DATA, soundItem)
////                startActivity(intent)
////            }
////        }
//        binding?.rcvSound?.layoutManager = gridLayoutManager
//        binding?.rcvSound?.adapter = soundAdapter
//    }
//
//    override fun addEvent() {
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        loadInter()
//
//    }
//
//    private fun loadInter() {
////        if (AdCache.getInstance().interSound == null) {
////            AdmobManager.getInstance()
////                .loadInterAds(requireActivity(), BuildConfig.inter_sound, object : AdCallback() {
////                    override fun onResultInterstitialAd(interstitialAd: InterstitialAd) {
////                        super.onResultInterstitialAd(interstitialAd)
////                        AdCache.getInstance().interSound = interstitialAd
////                    }
////                })
////        }
//    }
//}