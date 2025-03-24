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
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preferenceUtils: PreferenceUtils,
    networkHelper: NetworkHelper,
    workManager: WorkManager
) : WorkerViewModel(workManager = workManager, networkHelper = networkHelper) {

    private var languageState = MutableStateFlow(preferenceUtils.currentLanguage)
    val languageStateData: LiveData<String> get() = languageState.asLiveData()

    private var blackoutState = MutableStateFlow(preferenceUtils.blackoutActiveStatus)
    val blackoutStateData: LiveData<Boolean> get() = blackoutState.asLiveData()

    private var validateLicenseKeyState = MutableStateFlow(false)
    val validateLicenseKeyStateData: LiveData<Boolean> get() = validateLicenseKeyState.asLiveData()

    fun setLanguage(language: String) {
        viewModelScope.launch {
            preferenceUtils.currentLanguage = language
            languageState.emit(preferenceUtils.currentLanguage)
        }
    }

    fun setBlackoutPeriodStatus(status: Boolean) {
        viewModelScope.launch {
            preferenceUtils.blackoutActiveStatus = status
            blackoutState.emit(preferenceUtils.blackoutActiveStatus)
        }
    }

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
