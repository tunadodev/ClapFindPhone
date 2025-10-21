package com.ibl.tool.clapfindphone.main.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.ibl.tool.clapfindphone.base.BaseDialog
import com.ibl.tool.clapfindphone.databinding.DialogRecordPermissionBinding

open class RecordPermissionDialog(context: Context, val callback: (Boolean) -> Unit) :
    BaseDialog<DialogRecordPermissionBinding>(context, DialogRecordPermissionBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEvent()
    }

    fun setDialogCancellable(flag: Boolean): RecordPermissionDialog {
        setCancelable(flag)
        return this
    }
    private fun addEvent() {
        binding.btnDeny.setOnClickListener {
            logEvent("click_add_record_pms_deny")
            callback.invoke(false)
            dismiss()
        }
        binding.btnAllow.setOnClickListener {
            logEvent("click_add_record_pms_allow")
            callback.invoke(true)
            dismiss()
        }
    }

}