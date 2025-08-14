package org.intelehealth.data.provider.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intelehealth.common.service.BaseResponse
import org.intelehealth.common.service.HttpStatusCode.HTTP_OK
import org.intelehealth.common.state.StateWorker
import org.intelehealth.data.network.model.response.PullResponse
import org.intelehealth.data.provider.sync.data.SyncDataRepository

/**
 * Created by Vaghela Mithun R. on 10-04-2025 - 18:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltWorker
class DataRefreshWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted private val params: WorkerParameters,
    private val syncDataRepository: SyncDataRepository
) : StateWorker(ctx, params) {
    override suspend fun doWork(): Result {
        syncDataRepository.pushData().collect {
            handleState(it) { response -> withContext(Dispatchers.IO) { handleSuccessState(response) } }
        }
        return workerResult
    }

    private suspend fun handleSuccessState(response: BaseResponse<String, PullResponse>) {
        if (response.status == HTTP_OK) {
            response.data?.let {
                withContext(Dispatchers.IO) { updateSyncStatus(it) }
            } ?: setFailResult()
        } else setFailResult()
    }

    private suspend fun updateSyncStatus(data: PullResponse) {
        data.patients.map { it.toPatient() }.apply {
            syncDataRepository.updatePatients(this)
        }
    }
}
