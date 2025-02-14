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
    private val workManager: WorkManager, networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {
    private val mutableWorkerProgress: MutableLiveData<Int> = MutableLiveData()
    val workerProgress = mutableWorkerProgress.hide()

    fun startDataSync() {
        val configWorkRequest = OneTimeWorkRequestBuilder<SyncDataWorker>().build()
        workManager.enqueue(configWorkRequest)
        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(configWorkRequest.id).collect {
                it?.state?.name?.let { state ->
                    when (state) {
                        WorkInfo.State.SUCCEEDED.name -> mutableWorkerProgress.postValue(MAX_PROGRESS)

                        WorkInfo.State.FAILED.name -> {
                            it.outputData.getString(SyncDataWorker.WORK_STATUS)?.let { status ->
                                if (status == NO_NETWORK) dataConnectionStatus.postValue(false)
                                else errorResult.postValue(Throwable(status))
                            } ?: failResult.postValue(WorkInfo.State.FAILED.name)
                        }

                        else -> {
                            val progress = it.progress.getInt(SyncDataWorker.WORK_PROGRESS, 0)
                            mutableWorkerProgress.postValue(progress)
                        }
                    }
                }
            }
        }
    }
}
