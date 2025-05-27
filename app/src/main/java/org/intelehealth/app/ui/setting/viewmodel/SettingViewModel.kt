package org.intelehealth.app.ui.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.WorkerViewModel
import org.intelehealth.common.utility.PreferenceUtils
import org.intelehealth.data.provider.protocols.worker.DownloadProtocolsWorker
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 19-03-2025 - 18:45.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
/**
 * ViewModel responsible for managing application settings.
 *
 * This ViewModel handles settings-related operations, including:
 * - Managing the currently selected language.
 * - Controlling the blackout period status.
 * - Validating license keys for protocol downloads.
 * - Initiating the protocol download process.
 *
 * It interacts with [PreferenceUtils] to persist settings and uses
 * [WorkManager] to handle background tasks.
 */
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preferenceUtils: PreferenceUtils,
    networkHelper: NetworkHelper,
    workManager: WorkManager
) : WorkerViewModel(workManager = workManager, networkHelper = networkHelper) {

    private val languageState = MutableStateFlow(preferenceUtils.currentLanguage)
    val languageStateData: LiveData<String> = languageState.asLiveData()

    private val blackoutState = MutableStateFlow(preferenceUtils.blackoutActiveStatus)
    val blackoutStateData: LiveData<Boolean> = blackoutState.asLiveData()

    private val validateLicenseKeyState = MutableStateFlow(false)
    val validateLicenseKeyStateData: LiveData<Boolean> = validateLicenseKeyState.asLiveData()

    /**
     * Sets the application's language.
     *
     * This method updates the current language in [PreferenceUtils] and emits
     * the new language to [languageState].
     *
     * @param language The language code of the new language.
     */
    fun setLanguage(language: String) {
        viewModelScope.launch {
            preferenceUtils.currentLanguage = language
            languageState.emit(preferenceUtils.currentLanguage)
        }
    }

    /**
     * Sets the status of the blackout period.
     *
     * This method updates the blackout period status in [PreferenceUtils] and
     * emits the new status to [blackoutState].
     *
     * @param status `true` to enable the blackout period, `false` to disable it.
     */
    fun setBlackoutPeriodStatus(status: Boolean) {
        viewModelScope.launch {
            preferenceUtils.blackoutActiveStatus = status
            blackoutState.emit(preferenceUtils.blackoutActiveStatus)
        }
    }

    /**
     * Starts the protocol download worker.
     *
     * This method creates a [OneTimeWorkRequestBuilder] for the
     * [DownloadProtocolsWorker], sets the license key as input data, and
     * enqueues the work request using [enqueueOneTimeWorkRequest].
     *
     * @param licenseKey The license key required to download the protocols.
     */
    fun startDownloadProtocolWorker(licenseKey: String) {
        val inputData = Data.Builder()
            .putString(DownloadProtocolsWorker.PROTOCOL_LICENSE_KEY, licenseKey)
            .build()

        val protocolWorker = OneTimeWorkRequestBuilder<DownloadProtocolsWorker>()
            .setInputData(inputData).build()

        enqueueOneTimeWorkRequest(protocolWorker)
    }

    override fun handleOtherWorkStatus(status: String) {
        super.handleOtherWorkStatus(status)
        viewModelScope.launch {
            validateLicenseKeyState.emit(status == DownloadProtocolsWorker.PROTOCOL_LICENSE_KEY_VALIDATED)
            delay(500)
            validateLicenseKeyState.emit(false)
        }
    }
}
