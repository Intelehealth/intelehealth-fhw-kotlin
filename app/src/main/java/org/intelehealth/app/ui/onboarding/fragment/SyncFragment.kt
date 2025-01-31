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
    override val viewModel: SyncDataViewModel by viewModels<SyncDataViewModel>()
    private val args: SyncFragmentArgs by lazy { SyncFragmentArgs.fromBundle(requireArguments()) }

    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    override fun initiateProgressTask() {
        progressTitle(getString(ResourceR.string.content_welcome_user, args.userName))
        progressContent(getString(ResourceR.string.content_wait_for_sync))
        progressTask(getString(ResourceR.string.content_syncing))
        handleDataSyncProgress()
        viewModel.startDataSync()
    }

    private fun handleDataSyncProgress() {
        viewModel.workerProgress.observe(viewLifecycleOwner) {
            onProgress(it)
            if (it == 100) {
                progressTask(getString(ResourceR.string.content_synced))
                preferenceUtils.initialLaunchStatus = false
                findNavController().navigate(SyncFragmentDirections.actionSyncToHome())
            }
        }
    }

    override fun onError(reason: String) {
        super.onError(reason)
        errorMessage()
    }

//    private fun handleDataSyncError() {
//        viewModel.errorDataResult.observe(viewLifecycleOwner) {
//            errorMessage(getString(ResourceR.string.error_sync_failed_try_again))
//        }
//    }
//
//    private fun handleDataSyncConnectionError() {
//        viewModel.dataConnectionStatus.observe(viewLifecycleOwner) {
//            if (!it) errorMessage(getString(ResourceR.string.error_could_not_connect_with_server))
//        }
//    }

    override fun onRetry() {
        initiateProgressTask()
    }
}