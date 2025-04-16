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
import org.intelehealth.app.BuildConfig
import org.intelehealth.app.utility.KEY_FORCE_UPDATE_VERSION_CODE
import org.intelehealth.common.extensions.hide
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.common.utility.API_ERROR
import org.intelehealth.common.utility.NO_DATA_FOUND
import org.intelehealth.common.utility.NO_NETWORK
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.common.utility.WORKER_RESULT
import org.intelehealth.config.worker.ConfigSyncWorker
import org.intelehealth.fcm.utils.FcmRemoteConfig.getRemoteConfig
import org.intelehealth.fcm.utils.FcmTokenGenerator.getDeviceToken
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 07-01-2025 - 13:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

const val SPLASH_DELAY_TIME = 500L
const val ANIM_DURATION = 2000L

/**
 * ViewModel responsible for managing the application's initial launch and
 * configuration.
 *
 * This ViewModel handles tasks such as:
 * - Checking for force updates.
 * - Requesting initial configuration data.
 * - Determining if the user is logged in.
 * - Updating the Firebase Cloud Messaging (FCM) token.
 */
@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val preferenceUtils: PreferenceUtils,
    private val workManager: WorkManager,
    private val networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    private val mutableInitialLaunchStatus: MutableLiveData<Boolean> = MutableLiveData()
    val initialLaunchStatus = mutableInitialLaunchStatus.hide()

    /**
     * Updates the FCM token in shared preferences.
     *
     * This method retrieves the device's FCM token and saves it using
     * [PreferenceUtils].
     */
    fun updateFcmToken() = getDeviceToken { token: String? -> token?.let { preferenceUtils.fcmToken = token } }

    /**
     * Checks for a mandatory app update.
     *
     * This method retrieves the force update version code from the remote
     * configuration. If the current app's version code is lower than the
     * required version code, it invokes the provided callback. Otherwise, it
     * requests the application configuration.
     *
     * @param onNewUpdateAvailable Callback to be invoked if a new update is
     *                             available.
     */
    fun checkForceUpdate(onNewUpdateAvailable: () -> Unit) {
        if (isInternetAvailable()) {
            getRemoteConfig {
                val forceUpdateVersionCode = it.getLong(KEY_FORCE_UPDATE_VERSION_CODE)
                if (forceUpdateVersionCode > BuildConfig.VERSION_CODE) onNewUpdateAvailable.invoke()
                else requestConfig()
            }
        } else dataConnectionStatus.postValue(false)
    }

    /**
     * Requests the application configuration.
     *
     * If it's the initial launch (as determined by [PreferenceUtils]), this
     * method enqueues a [ConfigSyncWorker] to fetch the configuration data in
     * the background. It observes the worker's progress and updates the
     * [initialLaunchStatus] LiveData accordingly. If the worker fails, it
     * posts an error to the appropriate LiveData (e.g.,
     * [dataConnectionStatus], [errorResult], or [failResult]).
     *
     * If it's not the initial launch, it posts `false` to
     * [initialLaunchStatus] after a delay.
     */
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
                            when (workResult) {
                                API_ERROR -> errorResult.postValue(Throwable(API_ERROR))
                                NO_NETWORK -> dataConnectionStatus.postValue(false)
                                else -> failResult.postValue(NO_DATA_FOUND)
                            }
                        }
                    }
                }
            }
        } else Handler(Looper.getMainLooper()).postDelayed(
            { mutableInitialLaunchStatus.postValue(false) }, SPLASH_DELAY_TIME
        )
    }

    /**
     * Checks if the user is currently logged in.
     *
     * @return `true` if the user is logged in, `false` otherwise.
     */
    fun isUserLoggedIn() = preferenceUtils.userLoggedInStatus
}
