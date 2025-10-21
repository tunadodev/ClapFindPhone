package com.ibl.tool.clapfindphone.main.dialog

import android.content.Context
import android.os.Bundle
import com.ibl.tool.clapfindphone.base.BaseDialog
import com.ibl.tool.clapfindphone.databinding.DialogSelectSoundBinding

class SelectSoundDialog(context: Context, val callback: () -> Unit) :
    BaseDialog<DialogSelectSoundBinding>(context, DialogSelectSoundBinding::inflate) {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEvent()
    }

    private fun addEvent() {
        binding.tvOk.setOnClickListener {
            callback.invoke()
            dismiss()
        }
    }
}


