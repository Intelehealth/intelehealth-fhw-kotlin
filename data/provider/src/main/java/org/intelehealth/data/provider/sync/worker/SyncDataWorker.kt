package org.intelehealth.data.provider.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.github.ajalt.timberkt.Timber
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.common.service.BaseResponse
import org.intelehealth.common.service.HttpStatusCode.HTTP_OK
import org.intelehealth.common.service.HttpStatusCode.HTTP_SUCCESS
import org.intelehealth.common.state.StateWorker
import org.intelehealth.data.network.model.response.PullResponse
import org.intelehealth.data.provider.sync.data.SyncDataRepository

/**
 * Created by Vaghela Mithun R. on 25-10-2024 - 16:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted private val params: WorkerParameters,
    private val syncDataRepository: SyncDataRepository
) : StateWorker(ctx, params) {
    private var progress = START_PROGRESS
    private var currentPage = START_PAGE

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        setProgress(workDataOf(WORK_PROGRESS to progress))
        pullData(currentPage)
        workerResult
    }

    private suspend fun pullData(pageNo: Int) {
        syncDataRepository.pullData(pageNo).collect {
            handleState(it) { response -> withContext(Dispatchers.IO) { handleSuccessState(response) } }
        }
    }

    private suspend fun handleSuccessState(response: BaseResponse<String, PullResponse>) {
        if (response.status == HTTP_OK) {
            response.data?.let { saveData(it) } ?: setFailResult()
        } else setFailResult()
    }

    private suspend fun saveData(data: PullResponse) {
        syncDataRepository.saveData(data) { totalCount, page ->
            if (page > 0 && totalCount > 0) {

                if (currentPage < page) currentPage = page
                else if (currentPage == page) currentPage++
                else currentPage = page
                Timber.d { "currentPage => $currentPage" }
//                43a68f64-38f3-483c-b0f4-cef92b268669
                val percentage = (data.patients.size * 100) / totalCount
                progress += percentage
                setProgress(workDataOf(WORK_PROGRESS to progress))
                withContext(Dispatchers.IO) { pullData(currentPage) }

            } else {
                syncDataRepository.preferenceUtils.lastSyncedTime = data.pullExecutedTime
                workerResult = Result.success()
            }
        }
    }

    companion object {
        const val START_PROGRESS = 0
        const val START_PAGE = 0
    }
}
