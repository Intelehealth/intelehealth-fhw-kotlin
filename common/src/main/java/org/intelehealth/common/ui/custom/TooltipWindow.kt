package org.intelehealth.common.ui.custom

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.github.ajalt.timberkt.Timber
import org.intelehealth.common.databinding.TooltipLayoutBinding

/**
 * Created by Vaghela Mithun R. on 18-01-2025 - 12:45.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class TooltipWindow(private val context: Context) : PopupWindow(context) {
    private val binding by lazy { TooltipLayoutBinding.inflate(LayoutInflater.from(context)) }

    init {
        height = ActionBar.LayoutParams.WRAP_CONTENT
        width = ActionBar.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isTouchable = true
        isFocusable = true
        contentView = binding.root
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showTooltip(anchor: View, message: String) {
        binding.tvTooltipContent.text = message
        val anchorPosition = IntArray(2)
        anchor.getLocationOnScreen(anchorPosition)
        val anchorRect = Rect(
            anchorPosition[0], anchorPosition[1], anchorPosition[0] + anchor.width, anchorPosition[1] + anchor.height
        )
        contentView.measure(height, width)
//        val contentViewHeight = contentView.measuredHeight
        val contentViewWidth = contentView.measuredWidth
        val positionX = anchorRect.centerX() - (contentViewWidth / 2)
        val positionY = anchorRect.bottom - (anchorRect.height() / 2)
        showAtLocation(anchor, Gravity.NO_GRAVITY, positionX, positionY)
        handler.sendEmptyMessageDelayed(MSG_DISMISS_TOOLTIP, 4000)
        Timber.d { "showTooltip : $message" }
    }


    private var handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == MSG_DISMISS_TOOLTIP) {
                if (isShowing) dismiss()
            }
        }
    }

    companion object {
        const val MSG_DISMISS_TOOLTIP = 1
    }
}
