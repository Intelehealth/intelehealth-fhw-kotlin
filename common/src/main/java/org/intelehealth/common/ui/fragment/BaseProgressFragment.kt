package org.intelehealth.common.ui.fragment

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import org.intelehealth.common.databinding.ViewProgressBinding
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.extensions.show

/**
 * Created by Vaghela Mithun R. on 27-01-2025 - 19:10.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@SuppressLint("ClickableViewAccessibility")
abstract class BaseProgressFragment(@LayoutRes layoutResId: Int) : StateFragment(layoutResId), View.OnTouchListener {
    private lateinit var progressBinding: ViewProgressBinding
    private lateinit var progressPage: View

    fun bindProgressView(progressBinding: ViewProgressBinding) {
        this.progressBinding = progressBinding
        this.progressBinding.progressLayout.isClickable = false
        this.progressBinding.progressLayout.setOnClickListener(null)
        this.progressBinding.progressLayout.setOnTouchListener(this)
    }

    fun bindProgressView(progressBinding: ViewProgressBinding, progressPage: View) {
        this.progressBinding = progressBinding
        this.progressBinding.progressLayout.isClickable = false
        this.progressBinding.progressLayout.setOnClickListener(null)
        this.progressBinding.progressLayout.setOnTouchListener(this)

        this.progressPage = progressPage
        this.progressPage.isClickable = false
        this.progressPage.setOnClickListener(null)
        this.progressPage.setOnTouchListener(this)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return true
    }

    override fun showLoading() {
        super.showLoading()
        if (::progressBinding.isInitialized) progressBinding.progressLayout.show()
    }

    override fun hideLoading() {
        super.hideLoading()
        if (::progressBinding.isInitialized) progressBinding.progressLayout.hide()
    }

    override fun showPageLoading() {
        super.showPageLoading()
        if(::progressPage.isInitialized) progressPage.show()
    }

    override fun hidePageLoading() {
        super.hidePageLoading()
        if(::progressPage.isInitialized) progressPage.hide()
    }


    override fun onConnectionLost() {
        super.onConnectionLost()
        hideLoading()
        hidePageLoading()
    }
}
