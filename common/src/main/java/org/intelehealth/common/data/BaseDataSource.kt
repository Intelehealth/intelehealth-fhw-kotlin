package org.intelehealth.common.data

import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.utility.NO_NETWORK
import retrofit2.Response
import org.intelehealth.common.state.Result
import org.intelehealth.common.utility.API_ERROR

/**
 * Abstract base class for data sources with built-in error handling and network
 * connectivity checks.
 *
 * This class provides a common structure for data sources that interact with
 * remote APIs. It encapsulates error handling logic, including checks for
 * successful responses, network connectivity, and provides a consistent way to
 * emit results as a [kotlinx.coroutines.flow.Flow].
 *
 * @param dispatcher The [CoroutineDispatcher] to use for executing the API call.
 *   Defaults to [Dispatchers.IO].
 * @param networkHelper An optional [NetworkHelper] instance for checking network
 *   connectivity. If not provided, network checks will always return false.
 */
abstract class BaseDataSource(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val networkHelper: NetworkHelper? = null
) {
    companion object {
        const val NOT_FOUND_404 = 404
        const val BAD_REQUEST_400 = 400
    }

    /**
     * Executes an API call and returns the result as a [kotlinx.coroutines.flow.Flow].
     *
     * This function handles the following:
     * - Checks for network connectivity using the provided [NetworkHelper].
     * - Executes the provided [call] within a [kotlinx.coroutines.flow.flow] block.
     * - Emits [Result.Loading] at the start of the flow.
     * - If the network is available:
     *   - Executes the API call.
     *   - If the response is successful (HTTP status code 2xx):
     *     - Emits [Result.Success] with the response body and message.
     *   - If the response is not successful:
     *     - Emits [Result.Error] with the error message from the response.
     * - If the network is not available:
     *   - Logs a "No network" message using Timber.
     *   - Emits [Result.Fail] with a "No network" message.
     * - The flow is executed on the specified [dispatcher] (defaulting to
     *   [Dispatchers.IO]).
     *
     * @param T The type of the expected response body.
     * @param call A suspend function representing the API call, which should
     *   return a [retrofit2.Response] of type [T].
     * @return A [kotlinx.coroutines.flow.Flow] emitting [Result] objects
     *   representing the different states of the API call (Loading, Success,
     *   Error, Fail).
     */
    protected fun <T> getResult(call: suspend () -> Response<T>) = flow {
        if (isInternetAvailable()) {
            val response = call()
            if (response.isSuccessful) {
                println("API SUCCESS")
                val result = Result.Success(
                    response.body(), response.message()
                )
                emit(result)
            } else {
                println("API ERROR ${response.message()}")
                emit(Result.Error(response.message()))
            }
        } else {
            Timber.d { NO_NETWORK }
            emit(Result.Fail(NO_NETWORK))
        }
    }.onStart {
        emit(Result.Loading("Please wait..."))
    }.catch { e ->
        Timber.e(e) // Log the error
        emit(Result.Error(e.message ?: API_ERROR))
    }.flowOn(dispatcher)

    /**
     * Checks if the internet connection is available.
     *
     * This function uses the provided [NetworkHelper] (if available) to check
     * for network connectivity.
     *
     * @return `true` if the internet connection is available, `false` otherwise.
     *   Returns `false` if no [NetworkHelper] is provided.
     */
    private fun isInternetAvailable(): Boolean = networkHelper?.isNetworkConnected() ?: false
}
