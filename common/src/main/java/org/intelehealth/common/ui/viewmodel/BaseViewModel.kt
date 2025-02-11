package org.intelehealth.common.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import org.intelehealth.common.helper.NetworkHelper
import retrofit2.Response
import org.intelehealth.common.state.Result
import org.intelehealth.common.service.BaseResponse
import org.intelehealth.common.service.HttpStatusCode
import org.intelehealth.common.utility.NO_NETWORK
import timber.log.Timber

open class BaseViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO, private val networkHelper: NetworkHelper? = null
) : ViewModel() {
    private val loadingData = MutableLiveData<Boolean>()

    @JvmField
    var loading: LiveData<Boolean> = loadingData

    protected val failResult = MutableLiveData<String>()

    @JvmField
    var failDataResult: LiveData<String> = failResult

    protected val errorResult = MutableLiveData<Throwable>()

    @JvmField
    var errorDataResult: LiveData<Throwable> = errorResult

    var dataConnectionStatus = MutableLiveData<Boolean>(true)


    fun <L> executeLocalQuery(
        queryCall: () -> L?
    ) = flow {
        val localData = queryCall.invoke()
        localData?.let { emit(Result.Success(localData, "")) } ?: kotlin.run {
            emit(Result.Error<L>("No record found"))
        }
    }.onStart {
        emit(Result.Loading<L>("Please wait..."))
    }.flowOn(dispatcher)

    fun <S, R> executeNetworkCall(
        networkCall: suspend () -> Response<out BaseResponse<S, R>>
    ) = flow {
        if (isInternetAvailable()) {
            com.github.ajalt.timberkt.Timber.d { "network call started" }
            val response = networkCall()
            com.github.ajalt.timberkt.Timber.d { "response.status => ${response.code()}" }
            if (response.code() == HttpStatusCode.OK) {
                Timber.d("Api success")
                if (response.body()?.data != null && response.body()?.status is Boolean) {
                    if (response.body()?.status as Boolean) {
                        val result = Result.Success(response.body()?.data, "Success")
                        result.message = response.body()?.message
                        emit(result)
                        return@flow
                    } else {
                        emit(Result.Fail(response.body()?.message))
                        return@flow
                    }
                } else {
                    val result = Result.Success(response.body()?.data, "Success")
                    result.message = response.body()?.message
                    emit(result)
                    return@flow
                }
            } else {
                Timber.e("Api error ${response.body()?.message}")
                emit(Result.Error(response.body()?.message))
            }
        } else dataConnectionStatus.postValue(false)
    }.onStart {
        emit(Result.Loading("Please wait..."))
    }.flowOn(dispatcher)

    fun executeLocalInsertUpdateQuery(
        queryCall: () -> Boolean
    ) = flow {
        val status = queryCall.invoke()
        if (status) emit(Result.Success(true, ""))
        else emit(Result.Error<Boolean>("Failed"))
    }.onStart {
        emit(Result.Loading<Boolean>("Please wait..."))
    }.flowOn(dispatcher)

    fun <L, T> catchNetworkData(
        networkCall: suspend () -> Response<out BaseResponse<String, T>>, saveDataCall: suspend (T?) -> L
    ) = flow {
        com.github.ajalt.timberkt.Timber.d { "catchNetworkData" }
        if (isInternetAvailable()) {
            com.github.ajalt.timberkt.Timber.d { "catchNetworkData api calling" }
            val response = networkCall()
            if (response.code() == HttpStatusCode.OK) {
                Timber.d("Api success")
                val savedData = saveDataCall(response.body()?.data)
                val result = Result.Success(savedData, "Success")
                result.message = response.body()?.message
                emit(result)
            } else {
                Timber.e("Api error ${response.body()?.message}")
                emit(Result.Error(response.body()?.message))
            }
        } else dataConnectionStatus.postValue(false)
    }.onStart {
        emit(Result.Loading("Please wait..."))
    }.flowOn(dispatcher)

    fun isInternetAvailable(): Boolean = networkHelper?.isNetworkConnected() ?: false

    /**
     * Handle response here in base with loading and error message
     *
     */
    fun <T> handleResponse(it: Result<T>, callback: (data: T) -> Unit) {
        println("handleResponse status ${it.status} ${it.message}")
        when (it.status) {
            Result.State.SUCCESS -> {
                loadingData.postValue(false)
                it.data?.let { data ->
                    println("data ${Gson().toJson(data)}")
                    callback(data)
                } ?: failResult.postValue(it.message ?: "")
            }

            else -> handleCommonStats(it)
        }
    }

    fun <T> allowNullDataResponse(it: Result<T>, callback: (data: T?) -> Unit) {
        println("handleResponse status ${it.status} ${it.message}")
        when (it.status) {
            Result.State.SUCCESS -> {
                loadingData.postValue(false)
                callback(it.data)
            }

            else -> handleCommonStats(it)
        }
    }

    private fun <T> handleCommonStats(it: Result<T>) {
        when (it.status) {
            Result.State.LOADING -> {
                loadingData.postValue(true)
            }

            Result.State.FAIL -> {
                loadingData.postValue(false)
                if (it.message == NO_NETWORK) dataConnectionStatus.postValue(false)
                else failResult.postValue(it.message ?: "")
            }

            Result.State.ERROR -> {
                println("ERROR ${it.message}")
                loadingData.postValue(false)
                errorResult.postValue(Throwable(it.message))
            }

            else -> {
                // do nothing
            }
        }
    }
}
