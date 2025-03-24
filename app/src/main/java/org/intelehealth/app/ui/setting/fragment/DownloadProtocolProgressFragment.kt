package org.intelehealth.app.ui.setting.fragment

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.ui.setting.viewmodel.SettingViewModel
import org.intelehealth.common.extensions.showToast
import org.intelehealth.common.ui.fragment.CircularProgressFragment
import org.intelehealth.common.utility.CommonConstants.MAX_PROGRESS
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class DownloadProtocolProgressFragment : CircularProgressFragment() {
    override val viewModel: SettingViewModel by activityViewModels<SettingViewModel>()

    override fun initiateProgressTask() {
        progressTitle(getString(ResourceR.string.content_changing_protocols))
        progressContent(getString(ResourceR.string.content_wait_while_protocols_changing))
        progressTask(getString(ResourceR.string.content_downloading))
        handleDataSyncProgress()
    }

    private fun handleDataSyncProgress() {
        viewModel.workerProgress.observe(viewLifecycleOwner) {
            onProgress(it)
            if (it == MAX_PROGRESS) {
                progressTask(getString(ResourceR.string.content_download_completed))
                findNavController().popBackStack()
                showToast(ResourceR.string.content_protocols_successfully_changed)
            }
        }
    }

    override fun onError(reason: String) {
        super.onError(reason)
        errorMessage()
    }

    override fun onRetry() {
        initiateProgressTask()
    }
}
