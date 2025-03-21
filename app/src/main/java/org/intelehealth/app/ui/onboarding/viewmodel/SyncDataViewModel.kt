package org.intelehealth.app.ui.onboarding.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.ui.viewmodel.WorkerViewModel
import org.intelehealth.common.utility.CommonConstants.MAX_PROGRESS
import org.intelehealth.data.network.constants.NO_NETWORK
import org.intelehealth.data.provider.sync.worker.SyncDataWorker
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-01-2025 - 16:53.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class SyncDataViewModel @Inject constructor(
    networkHelper: NetworkHelper,
    workManager: WorkManager
) : WorkerViewModel(workManager = workManager, networkHelper = networkHelper) {

    fun startDataSync() {
        val configWorkRequest = OneTimeWorkRequestBuilder<SyncDataWorker>().build()
        enqueueOneTimeWorkRequest(configWorkRequest)
    }
}
