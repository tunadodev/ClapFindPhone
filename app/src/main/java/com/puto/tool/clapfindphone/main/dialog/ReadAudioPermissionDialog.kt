package com.puto.tool.clapfindphone.main.dialog

import android.content.Context
import android.os.Bundle
import com.puto.tool.clapfindphone.base.BaseDialog
import com.puto.tool.clapfindphone.databinding.DialogReadAudioPermissionBinding

open class ReadAudioPermissionDialog(context: Context, val callback: (Boolean) -> Unit) :
    BaseDialog<DialogReadAudioPermissionBinding>(context, DialogReadAudioPermissionBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEvent()
    }

    fun setDialogCancellable(flag: Boolean): ReadAudioPermissionDialog {
        setCancelable(flag)
        return this
    }
    private fun addEvent() {
        binding.btnDeny.setOnClickListener {
            logEvent("click_add_import_pms_deny")
            callback.invoke(false)
            dismiss()
        }
        binding.btnAllow.setOnClickListener {
            logEvent("click_add_import_pms_allow")
            callback.invoke(true)
            dismiss()
        }
    }
}