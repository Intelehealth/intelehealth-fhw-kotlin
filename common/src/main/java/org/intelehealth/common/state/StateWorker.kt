package org.intelehealth.common.state

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import org.intelehealth.common.state.Result.State
import org.intelehealth.common.utility.API_ERROR
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.common.utility.NO_NETWORK
import org.intelehealth.common.state.Result as StateResult

/**
 * Created by Vaghela Mithun R. on 20-03-2025 - 16:49.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class StateWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    protected var workerResult = Result.failure()
    suspend fun <T> handleState(state: StateResult<T>, callback: suspend (data: T) -> Unit) {
        when (state.status) {
            State.FAIL -> setNetworkFailResult()

            State.ERROR -> setErrorResult()

            State.SUCCESS -> state.data?.let { data ->
                println("data ${Gson().toJson(data)}")
                callback(data)
            } ?: setFailResult()

            State.LOADING -> {
                // Do nothing
            }
        }
    }

    private fun setNetworkFailResult() {
        val data = workDataOf(WORK_STATUS to NO_NETWORK)
        workerResult = Result.failure(data)
    }

    private fun setErrorResult() {
        val data = workDataOf(WORK_STATUS to API_ERROR)
        workerResult = Result.failure(data)
    }

    private fun setFailResult() {
        val data = workDataOf(WORK_STATUS to NO_DATA_FOUND)
        workerResult = Result.failure(data)
    }

    companion object {
        const val WORK_PROGRESS = "work_progress"
        const val WORK_STATUS = "work_status"
    }
}
