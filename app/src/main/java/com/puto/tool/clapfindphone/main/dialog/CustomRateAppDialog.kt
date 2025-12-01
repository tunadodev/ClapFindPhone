package com.puto.tool.clapfindphone.main.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.widget.EditText
import com.puto.tool.clapfindphone.R
import com.ymb.ratingbar_lib.RatingBar

class CustomRateAppDialog(context: Context?) : Dialog(context!!) {
    private var handler: Handler? = null
    private var edtContent: EditText? = null
    private var rd: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_custom_rate)
    }

    override fun show() {
        super.show()
        initView()
    }

    private fun initView() {
        setCanceledOnTouchOutside(false)
        val rating = findViewById<RatingBar>(R.id.rating)
        edtContent = findViewById(R.id.edt_content)
        setOnDismissListener { dialogInterface: DialogInterface? -> }


    }

    override fun onBackPressed() {}
}