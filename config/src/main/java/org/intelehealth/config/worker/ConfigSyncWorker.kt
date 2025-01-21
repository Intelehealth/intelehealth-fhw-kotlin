package org.intelehealth.config.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.room.PrimaryKey
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.config.data.ConfigRepository
import org.intelehealth.config.utility.NO_DATA_FOUND
import org.intelehealth.config.utility.WORKER_RESULT

/**
 * Created by Vaghela Mithun R. on 12-04-2024 - 13:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltWorker
class ConfigSyncWorker @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
    private val configRepository: ConfigRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineWorker(ctx, params) {
    private var wokerResult = Result.failure()
    override suspend fun doWork(): Result {
        withContext(dispatcher) {
            configRepository.suspendFetchAndUpdateConfig().collect { result ->
                if (result.isSuccess()) {
                    result.data?.let {
                        configRepository.saveAllConfig(it, this) { wokerResult = Result.success() }
                    } ?: setFailureResult()
                } else setFailureResult()
            }
        }

        return wokerResult
    }

    private fun setFailureResult() {
        wokerResult = Result.failure(workDataOf(Pair(WORKER_RESULT, NO_DATA_FOUND)))
    }

    companion object {
        fun startConfigSyncWorker(context: Context, onResult: (String) -> Unit) {
            val configWorkRequest = OneTimeWorkRequestBuilder<ConfigSyncWorker>().build()
            val workManager = WorkManager.getInstance(context.applicationContext)
            workManager.enqueue(configWorkRequest)
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            scope.launch {
                workManager.getWorkInfoByIdFlow(configWorkRequest.id).collect {
                    Timber.d { "startConfigSyncWorker: ${Gson().toJson(it?.outputData)}" }
                    onResult(it?.state?.name!!)
                }
            }
        }
    }
}