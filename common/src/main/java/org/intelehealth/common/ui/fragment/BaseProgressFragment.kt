package org.intelehealth.common.ui.fragment

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.intelehealth.common.databinding.ViewProgressBinding
import android.view.View
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.extensions.show

/**
 * Created by Vaghela Mithun R. on 27-01-2025 - 19:10.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@SuppressLint("ClickableViewAccessibility")
open class BaseProgressFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId), View.OnTouchListener {
    private lateinit var progressBinding: ViewProgressBinding

    fun bindProgressView(progressBinding: ViewProgressBinding) {
        this.progressBinding = progressBinding
        this.progressBinding.progressLayout.isClickable = false
        this.progressBinding.progressLayout.setOnClickListener(null)
        this.progressBinding.progressLayout.setOnTouchListener(this)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return true
    }

    fun showProgress() = progressBinding.progressLayout.show()

    fun hideProgress() = progressBinding.progressLayout.hide()
}