package com.ibl.tool.clapfindphone.main.dialog

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.base.BaseDialog
import com.ibl.tool.clapfindphone.data.repo.AppRepository
import com.ibl.tool.clapfindphone.databinding.DialogRenameBinding

open class RenameDialog(context: Context, val callback: (Boolean, String) -> Unit) :
    BaseDialog<DialogRenameBinding>(context, DialogRenameBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEvent()
    }

    private fun addEvent() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDone.setOnClickListener {
            val name = binding.edtName.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.name_sound_is_empty), Toast.LENGTH_SHORT).show()
            } else if (AppRepository.checkHasSound(name)) {
                Toast.makeText(context, context.getString(R.string.name_sound_already_exists), Toast.LENGTH_SHORT).show()
            } else {
                callback.invoke(true, name)
            }
            dismiss()
        }
    }
}