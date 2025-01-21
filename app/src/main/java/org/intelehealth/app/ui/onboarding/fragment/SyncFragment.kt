package org.intelehealth.app.ui.onboarding.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.ui.onboarding.viewmodel.SyncDataViewModel
import org.intelehealth.common.ui.fragment.CircularProgressFragment
import org.intelehealth.common.utility.PreferenceUtils
import javax.inject.Inject
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@AndroidEntryPoint
class SyncFragment : CircularProgressFragment() {
    private val syncDataViewModel by viewModels<SyncDataViewModel>()
    private val args: SyncFragmentArgs by lazy { SyncFragmentArgs.fromBundle(requireArguments()) }

    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    override fun initiateProgressTask() {
        progressTitle(getString(ResourceR.string.content_welcome_user, args.userName))
        progressContent(getString(ResourceR.string.content_wait_for_sync))
        progressTask(getString(ResourceR.string.content_syncing))
        handleDataSyncProgress()
        handleDataSyncError()
        handleDataSyncConnectionError()
        syncDataViewModel.startDataSync()
    }

    private fun handleDataSyncProgress() {
        syncDataViewModel.workerProgress.observe(viewLifecycleOwner) {
            onProgress(it)
            if (it == 100) {
                progressTask(getString(ResourceR.string.content_synced))
                preferenceUtils.initialLaunchStatus = false
                findNavController().navigate(SyncFragmentDirections.actionSyncToHome())
            }
        }
    }

    private fun handleDataSyncError() {
        syncDataViewModel.errorDataResult.observe(viewLifecycleOwner) {
            errorMessage(getString(ResourceR.string.error_sync_failed_try_again))
        }
    }

    private fun handleDataSyncConnectionError() {
        syncDataViewModel.dataConnectionStatus.observe(viewLifecycleOwner) {
            if (!it) errorMessage(getString(ResourceR.string.error_could_not_connect_with_server))
        }
    }

    override fun onRetry() {
        initiateProgressTask()
    }

    override fun onClose() {
        requireActivity().finish()
    }

}