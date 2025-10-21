package com.ibl.tool.clapfindphone.main.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.ibl.tool.clapfindphone.BuildConfig
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.databinding.ActivityPolicyWebviewBinding
import com.ibl.tool.clapfindphone.onboard_flow.base.BaseObdActivity

class PolicyWebViewActivity : BaseObdActivity<ActivityPolicyWebviewBinding>() {

    companion object {
        private const val POLICY_URL = "https://sites.google.com/view/coloring-privacypolicy/home"
        
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, PolicyWebViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutActivity(): Int {
        return R.layout.activity_policy_webview
    }

    override fun initViews() {
        nameView = "policy_screen"
        setupUI()
        addEvent()
    }

    private fun addEvent() {
        viewBinding.ivBack.setOnClickListener {
            onBackPressed()
        }
        
        viewBinding.tvTerm.setOnClickListener {
            openUrl(POLICY_URL)
        }
    }

    private fun setupUI() {
        viewBinding.tvVersion.text = "${getString(R.string.version)} ${BuildConfig.VERSION_NAME}"
    }

    private fun openUrl(strUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUrl))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        try {
            startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            Log.d("PolicyWebViewActivity", "Cannot open URL: $e")
        }
    }
}
