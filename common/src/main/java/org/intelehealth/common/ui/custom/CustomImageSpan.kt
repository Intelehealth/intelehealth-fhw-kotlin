package org.intelehealth.common.ui.custom

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 * Created by Vaghela Mithun R. on 20-01-2025 - 13:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class CustomImageSpan(drawable: Drawable, private val verticalAlignment: Int) : ImageSpan(drawable) {

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        val drawable = drawable
        canvas.save()

        val transY = when (verticalAlignment) {
            ALIGN_BASELINE -> y - drawable.bounds.bottom
            ALIGN_BOTTOM -> bottom - drawable.bounds.bottom
            else -> (bottom - top) / 2 - drawable.bounds.height() / 2 + top
        }

        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }

    companion object {
        const val ALIGN_CENTER = 2
    }
}