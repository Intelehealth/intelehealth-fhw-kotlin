package org.intelehealth.app.ui.onboarding.viewmodel

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.WorkerViewModel
import org.intelehealth.data.provider.sync.worker.DataRefreshWorker
import org.intelehealth.data.provider.sync.worker.SyncDataWorker
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-01-2025 - 16:53.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * ViewModel responsible for initiating and managing the data synchronization
 * process.
 *
 * This ViewModel interacts with the WorkManager to enqueue a
 * [SyncDataWorker], which performs the actual data synchronization in the
 * background.
 */
@HiltViewModel
class SyncDataViewModel @Inject constructor(
    networkHelper: NetworkHelper,
    workManager: WorkManager
) : WorkerViewModel(workManager = workManager, networkHelper = networkHelper) {

    /**
     * Starts the data synchronization process by enqueuing a [SyncDataWorker].
     */
    fun startDataSync() {
        val configWorkRequest = OneTimeWorkRequestBuilder<SyncDataWorker>().build()
        enqueueOneTimeWorkRequest(configWorkRequest)
    }

    /**
     * Observes the status of the data synchronization worker.
     *
     * @return LiveData containing the list of [WorkInfo] objects representing
     * the status of the worker.
     */

    fun startPushDataWorker() {
        val configWorkRequest = OneTimeWorkRequestBuilder<DataRefreshWorker>().build()
        enqueueOneTimeWorkRequest(configWorkRequest)
    }
}
