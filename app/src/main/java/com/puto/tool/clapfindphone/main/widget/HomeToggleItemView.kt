package com.puto.tool.clapfindphone.main.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.puto.tool.clapfindphone.R
import com.puto.tool.clapfindphone.databinding.ViewHomeToggleItemBinding

class HomeToggleItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewHomeToggleItemBinding =
        ViewHomeToggleItemBinding.inflate(LayoutInflater.from(context), this, true)

    // Listener từ bên ngoài (HomeActivity)
    private var externalListener: SwitchButton.OnCheckedChangeListener? = null

    // Trạng thái mong muốn khi đồng bộ UI (set từ code),
    // để bỏ qua callback lần đầu từ animation nội bộ của SwitchButton
    private var pendingSyncState: Boolean? = null

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

    /**
     * Đồng bộ trạng thái switch từ code (ví dụ ở HomeActivity.setupViews).
     * Lần onCheckedChanged tương ứng với trạng thái này sẽ bị bỏ qua,
     * tránh việc callback kích hoạt điều hướng không mong muốn.
     */
    fun setChecked(checked: Boolean) {
        pendingSyncState = checked
        binding.toggleSwitch.setChecked(checked)
    }

    fun isChecked(): Boolean = binding.toggleSwitch.isChecked

    fun setOnCheckedChangeListener(listener: SwitchButton.OnCheckedChangeListener) {
        externalListener = listener
        binding.toggleSwitch.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton, isChecked: Boolean) {
                // Nếu đây là lần callback do việc sync trạng thái từ code, bỏ qua
                if (pendingSyncState != null && pendingSyncState == isChecked) {
                    pendingSyncState = null
                    return
                }
                externalListener?.onCheckedChanged(view, isChecked)
            }
        })
    }
}
