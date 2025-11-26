package com.ibl.tool.clapfindphone.main.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.ibl.tool.clapfindphone.R
import com.ibl.tool.clapfindphone.databinding.ViewHomeToggleItemBinding

class HomeToggleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewHomeToggleItemBinding =
        ViewHomeToggleItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.HomeToggleItemView, defStyleAttr, 0) {
            val title = getString(R.styleable.HomeToggleItemView_toggleTitle)
            val iconResId = getResourceId(R.styleable.HomeToggleItemView_toggleIcon, 0)

            if (!title.isNullOrEmpty()) {
                binding.title.text = title
            }

            if (iconResId != 0) {
                binding.icon.setImageResource(iconResId)
            }
        }
    }

    val switchView: SwitchButton
        get() = binding.toggleSwitch

    fun setChecked(checked: Boolean) {
        binding.toggleSwitch.setChecked(checked)
    }

    fun isChecked(): Boolean = binding.toggleSwitch.isChecked

    fun setOnCheckedChangeListener(listener: SwitchButton.OnCheckedChangeListener) {
        binding.toggleSwitch.setOnCheckedChangeListener(listener)
    }
}



