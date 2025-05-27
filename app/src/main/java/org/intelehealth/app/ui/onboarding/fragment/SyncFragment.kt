package org.intelehealth.app.ui.onboarding.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.ui.onboarding.viewmodel.SyncDataViewModel
import org.intelehealth.common.ui.fragment.CircularProgressFragment
import org.intelehealth.common.utility.CommonConstants.MAX_PROGRESS
import org.intelehealth.common.utility.PreferenceUtils
import javax.inject.Inject
import org.intelehealth.resource.R as ResourceR

/**
 * Created by Vaghela Mithun R. on 05-01-2025 - 12:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * A fragment that displays a circular progress indicator while synchronizing data.
 *
 * This fragment is responsible for showing a progress UI during data
 * synchronization. It interacts with the [SyncDataViewModel] to monitor the
 * synchronization progress and update the UI accordingly. Upon successful
 * synchronization, it navigates to the home screen and finishes the current
 * activity.
 */
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

    /**
     * Observes the data synchronization progress and updates the UI.
     *
     * This method observes the progress reported by the [SyncDataViewModel] and
     * updates the progress indicator. When the progress reaches 100%, it
     * updates the displayed task, sets the initial launch status in preferences
     * to false, navigates to the home screen, and finishes the current activity.
     */
    private fun handleDataSyncProgress() {
        viewModel.workerProgress.observe(viewLifecycleOwner) {
            onProgress(it)
            if (it == MAX_PROGRESS) {
                progressTask(getString(ResourceR.string.content_synced))
                preferenceUtils.initialLaunchStatus = false
                findNavController().navigate(SyncFragmentDirections.actionSyncToHome())
                requireActivity().finish()
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
