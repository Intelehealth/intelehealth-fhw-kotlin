package org.intelehealth.app.ui.onboarding.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.fcm.utils.FcmRemoteConfig.getRemoteConfig
import org.intelehealth.fcm.utils.FcmTokenGenerator.getDeviceToken
import javax.inject.Inject
import org.intelehealth.app.BuildConfig
import org.intelehealth.app.utility.KEY_FORCE_UPDATE_VERSION_CODE
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.utility.API_ERROR
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.common.utility.NO_NETWORK
import org.intelehealth.common.utility.WORKER_RESULT
import org.intelehealth.config.worker.ConfigSyncWorker
import kotlin.jvm.Throws

/**
 * Created by Vaghela Mithun R. on 07-01-2025 - 13:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val SPLASH_DELAY_TIME = 500L

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val preferenceUtils: PreferenceUtils,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    private val mutableInitialLaunchStatus: MutableLiveData<Boolean> = MutableLiveData()
    val initialLaunchStatus = mutableInitialLaunchStatus.hide()

    fun updateFcmToken() = getDeviceToken { token: String? -> token?.let { preferenceUtils.fcmToken = token } }

    fun checkForceUpdate(onNewUpdateAvailable: () -> Unit) {
        if (isInternetAvailable()) {
            getRemoteConfig {
                val forceUpdateVersionCode = it.getLong(KEY_FORCE_UPDATE_VERSION_CODE)
                if (forceUpdateVersionCode > BuildConfig.VERSION_CODE) onNewUpdateAvailable.invoke()
                else requestConfig()
            }
        } else dataConnectionStatus.postValue(false)
    }

    fun requestConfig() {
        if (isInternetAvailable()) if (preferenceUtils.initialLaunchStatus) {
            val configWorkRequest = OneTimeWorkRequestBuilder<ConfigSyncWorker>().build()
            workManager.enqueue(configWorkRequest)
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            scope.launch {
                workManager.getWorkInfoByIdFlow(configWorkRequest.id).collect {
                    Timber.d { "startConfigSyncWorker: ${Gson().toJson(it?.outputData)}" }
                    it?.state?.name?.let { state ->
                        if (state == WorkInfo.State.SUCCEEDED.name) {
                            mutableInitialLaunchStatus.postValue(true)
                        } else if (state == WorkInfo.State.FAILED.name) {
                            val workResult = it.outputData.getString(WORKER_RESULT)
                            if (workResult == NO_NETWORK) dataConnectionStatus.postValue(false)
                            else if (workResult == API_ERROR) errorResult.postValue(Throwable(API_ERROR))
                            else failResult.postValue(NO_DATA_FOUND)
                        }
                    }
                }
            }
        } else Handler(Looper.getMainLooper()).postDelayed(
            { mutableInitialLaunchStatus.postValue(false) }, SPLASH_DELAY_TIME
        )
    }

    fun isUserLoggedIn() = preferenceUtils.userLoggedInStatus
}