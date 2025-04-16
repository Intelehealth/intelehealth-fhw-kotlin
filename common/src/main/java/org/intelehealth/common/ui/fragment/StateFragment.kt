package org.intelehealth.common.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.github.ajalt.timberkt.Timber
import org.intelehealth.common.extensions.showErrorSnackBar
import org.intelehealth.common.extensions.showNetworkLostSnackBar
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.resource.R

/**
 * Created by Vaghela Mithun R. on 30-01-2025 - 11:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class StateFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    abstract val viewModel: BaseViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStateData()
    }

    private fun observeStateData() {
        viewModel.failDataResult.observe(viewLifecycleOwner) {
            it ?: return@observe
            onFailed(it)
        }

        viewModel.errorDataResult.observe(viewLifecycleOwner) {
            it ?: return@observe
            onError(it.message ?: getString(R.string.content_something_went_wrong))
        }

        viewModel.dataConnectionStatus.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (!it) onConnectionLost()
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            it ?: return@observe
            if (it) showLoading() else hideLoading()
        }
    }

    open fun getAnchorView(): View? = null
    open fun showLoading() {}
    open fun hideLoading() {}
    open fun retryOnNetworkLost() {
        Timber.d { "Retry on network lost" }
    }

    open fun onFailed(reason: String) {
        Timber.e { reason }
    }

    open fun onError(reason: String) {
        Timber.e { reason }
        showErrorSnackBar(getAnchorView(), R.string.content_something_went_wrong)
    }

    open fun onConnectionLost() {
        Timber.e { "onConnectionLost" }
        showNetworkLostSnackBar(
            getAnchorView(), R.string.error_could_not_connect_with_server
        ) { retryOnNetworkLost() }
    }
}
