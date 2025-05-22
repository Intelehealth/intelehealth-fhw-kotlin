package org.intelehealth.app.ui.patient.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel
import org.intelehealth.data.offline.entity.PatientOtherInfo
import org.intelehealth.data.provider.patient.otherinfo.PatientOtherDataRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 04-05-2025 - 12:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class PatientOtherInfoViewModel @Inject constructor(
    networkHelper: NetworkHelper,
    private val patientOtherInfoRepository: PatientOtherDataRepository,
    private val patientRegFieldRepository: RegFieldRepository
) : PatientViewModel(
    otherInfoRepository = patientOtherInfoRepository,
    regFieldRepository = patientRegFieldRepository,
    networkHelper = networkHelper
) {

    fun fetchPatientOtherInfo(patientId: String) = patientOtherInfoRepository.getPatientOtherAttrs(patientId)


    fun createPatientOtherInfo(otherInfo: PatientOtherInfo) = executeLocalQuery {
        patientOtherInfoRepository.createPatientOtherData(otherInfo.apply {
            patientMasterAttrs = patientMasterAttributes
        })
    }.asLiveData()

    fun updatePatientOtherInfo(patientId: String, otherInfo: PatientOtherInfo) = executeLocalQuery {
        patientOtherInfoRepository.updateOrInsertOtherAttributes(patientId, otherInfo.apply {
            patientMasterAttrs = patientMasterAttributes
        })
    }.asLiveData()
}
