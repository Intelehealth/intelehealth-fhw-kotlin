package org.intelehealth.common.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.state.StateWorker
import org.intelehealth.common.utility.CommonConstants.MAX_PROGRESS
import org.intelehealth.common.utility.NO_NETWORK
import java.util.UUID

/**
 * Created by Vaghela Mithun R. on 21-03-2025 - 11:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
open class WorkerViewModel(
    networkHelper: NetworkHelper,
    private val workManager: WorkManager
) : BaseViewModel(networkHelper = networkHelper) {

    private val mutableWorkerProgress: MutableLiveData<Int> = MutableLiveData()
    val workerProgress = mutableWorkerProgress.hide()

    fun enqueueOneTimeWorkRequest(request: OneTimeWorkRequest) {
        workManager.enqueue(request)
        viewModelScope.launch { getWorkInfoByIdFlow(request.id) }
    }

    private suspend fun getWorkInfoByIdFlow(requestId: UUID) {
        workManager.getWorkInfoByIdFlow(requestId).collect {
            it?.let { workInfo -> handleWorkInfoState(workInfo) }
        }
    }

    private fun handleWorkInfoState(workInfo: WorkInfo) {
        when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> handleSuccessState()
            WorkInfo.State.FAILED -> handleFailState(workInfo)
            else -> handleProgressState(workInfo)
        }
    }

    private fun handleSuccessState() {
        mutableWorkerProgress.postValue(MAX_PROGRESS)
        viewModelScope.launch {
            delay(500)
            mutableWorkerProgress.postValue(0)
        }
    }

    private fun handleFailState(workInfo: WorkInfo) {
        workInfo.outputData.getString(StateWorker.WORK_STATUS)?.let { status ->
            if (status == NO_NETWORK) dataConnectionStatus.postValue(false)
            else errorResult.postValue(Throwable(status))
        } ?: failResult.postValue(WorkInfo.State.FAILED.name)
    }

    private fun handleProgressState(workInfo: WorkInfo) {
        workInfo.progress.getString(StateWorker.WORK_STATUS)?.let { status ->
            handleOtherWorkStatus(status)
        } ?: setProgressInfo(workInfo)
    }

    private fun setProgressInfo(workInfo: WorkInfo) {
        val progress = workInfo.progress.getInt(StateWorker.WORK_PROGRESS, 0)
        mutableWorkerProgress.postValue(progress)
    }

    open fun handleOtherWorkStatus(status: String) {
        Timber.d { "handleOtherWorkStatus => $status" }
    }
}
