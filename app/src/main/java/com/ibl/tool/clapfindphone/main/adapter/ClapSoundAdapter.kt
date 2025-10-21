package com.ibl.tool.clapfindphone.main.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibl.tool.clapfindphone.ADAPTER_ADS_TYPE
import com.ibl.tool.clapfindphone.ADAPTER_ITEM_TYPE
import com.ibl.tool.clapfindphone.ADS_SOUND_TYPE
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.data.model.SoundItem
import com.ibl.tool.clapfindphone.databinding.ItemClapSoundBinding
import com.ibl.tool.clapfindphone.utils.app.AppPreferences

class ClapSoundAdapter(
    private val mList: List<SoundItem?>,
    private val activity: Activity,
    private val onItemClick: (SoundItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedSoundPath: String? = if (AppPreferences.instance.hasSelectedSound) {
        AppPreferences.instance.currentSound.soundPath
    } else {
        null // No sound selected yet
    }
    
    private var showGuide: Boolean = !AppPreferences.instance.hasSelectedSound

    fun setSelectedSound(soundItem: SoundItem) {
        val oldPath = selectedSoundPath
        selectedSoundPath = soundItem.soundPath
        
        // Notify changes for old and new selected items
        mList.forEachIndexed { index, item ->
            if (item?.soundPath == oldPath || item?.soundPath == selectedSoundPath) {
                notifyItemChanged(index)
            }
        }
    }
    
    fun setShowGuide(show: Boolean) {
        if (showGuide != show) {
            showGuide = show
            // Update first sound item
            mList.forEachIndexed { index, item ->
                if (item?.type != ADS_SOUND_TYPE) {
                    notifyItemChanged(index)
                    return
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position]?.type == ADS_SOUND_TYPE) {
            ADAPTER_ADS_TYPE
        } else {
            ADAPTER_ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ADAPTER_ADS_TYPE) {
            // For now, return a simple view holder for ads
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            object : RecyclerView.ViewHolder(view) {}
        } else {
            val binding = ItemClapSoundBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SoundViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemSound = mList[position] ?: return
        if (holder is SoundViewHolder) {
            // Check if this is the first sound item (not ads)
            var isFirstSoundItem = false
            if (showGuide) {
                for (i in 0 until mList.size) {
                    if (mList[i]?.type != ADS_SOUND_TYPE) {
                        isFirstSoundItem = (i == position)
                        break
                    }
                }
            }
            holder.loadData(itemSound, isFirstSoundItem)
        }
    }

    override fun getItemCount(): Int = mList.size

    inner class SoundViewHolder(private val binding: ItemClapSoundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun loadData(soundItem: SoundItem, isFirstItem: Boolean = false) {
            // Load image
            Glide.with(activity)
                .load(soundItem.image)
                .into(binding.ivImage)

            // Set name
            binding.tvName.text = soundItem.name

            // Set selection state
            val isSelected = soundItem.soundPath == selectedSoundPath
            if (isSelected) {
                binding.root.setBackgroundResource(R.drawable.bg_radius_12_strock)
                binding.viewSelected.visibility = View.VISIBLE
                binding.ivCheck.visibility = View.VISIBLE
            } else {
                binding.root.setBackgroundResource(R.drawable.bg_radius_12)
                binding.viewSelected.visibility = View.GONE
                binding.ivCheck.visibility = View.GONE
            }
            
            // Show/hide guide animation
            binding.lottieGuide.visibility = if (isFirstItem && showGuide) View.VISIBLE else View.GONE

            // Click listener - Open settings instead of direct selection
            binding.root.setOnClickListener {
                onItemClick(soundItem)
            }
        }
    }
}

