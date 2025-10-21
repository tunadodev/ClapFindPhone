package com.ibl.tool.clapfindphone.main.dialog

import android.content.Context
import android.os.Bundle
import android.text.Html
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.base.BaseDialog
import com.ibl.tool.clapfindphone.databinding.DialogDeleteBinding
import com.ibl.tool.clapfindphone.databinding.DialogExitBinding

open class ExitDialog(context: Context, val callback: () -> Unit) :
    BaseDialog<DialogExitBinding>(context, DialogExitBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvContent.text =
            Html.fromHtml(context.getString(R.string.are_you_sure_you_want_to_exit))
        addEvent()
    }

    private fun addEvent() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDelete.setOnClickListener {
            callback.invoke()
            dismiss()
        }
    }
}