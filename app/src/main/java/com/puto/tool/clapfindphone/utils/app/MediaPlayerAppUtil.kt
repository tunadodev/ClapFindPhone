package com.puto.tool.clapfindphone.utils.app

import android.content.Context
import com.puto.tool.clapfindphone.DEFAULT_SOUND_TYPE
import com.puto.tool.clapfindphone.data.model.SoundItem
import com.puto.tool.clapfindphone.utils.MediaPlayerUtil

object MediaPlayerAppUtil {
    fun playAudio(context: Context, soundItem: SoundItem, callback: () -> Unit) {
        if (soundItem.type == DEFAULT_SOUND_TYPE) {
            soundItem.soundPath?.let {
                MediaPlayerUtil.playAudioAssets(
                    context,
                    it.substring(it.lastIndexOf("/") + 1),
                    callback
                )
            }
        } else {
            soundItem.soundPath?.let { MediaPlayerUtil.playAudioPath(it, callback) }
        }
    }

    fun stopAudio() {
        MediaPlayerUtil.stopAudio()
    }
}