package org.intelehealth.app.ui.patient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.state.Result
import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.PatientOtherInfo
import org.intelehealth.data.provider.patient.otherinfo.PatientOtherDataRepository
import org.intelehealth.data.provider.patient.personal.PatientPersonalDataRepository
import org.intelehealth.data.provider.user.UserRepository
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-04-2025 - 17:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class PatientPersonalViewModel @Inject constructor(
    networkHelper: NetworkHelper,
    private val patientPersonalInfoRepository: PatientPersonalDataRepository,
    private val patientOtherInfoRepository: PatientOtherDataRepository,
    patientRegFieldRepository: RegFieldRepository,
    private val userRepository: UserRepository
) : PatientViewModel(
    otherInfoRepository = patientOtherInfoRepository,
    regFieldRepository = patientRegFieldRepository,
    networkHelper = networkHelper
) {
    /**
     * Fetches the personal information of a patient by their ID.
     *
     * @param patientId The ID of the patient whose information is to be fetched.
     * @return A LiveData object containing the patient's personal information.
     */
    fun fetchPersonalInfo(patientId: String) = patientPersonalInfoRepository.getPatientById(patientId)

    fun createPatient(patient: Patient, otherInfo: PatientOtherInfo) = executeLocalQuery {
        patientPersonalInfoRepository.insertPatient(patient)
    }.zip(executeLocalQuery {
        otherInfo.providerId = userRepository.getProviderId()
        otherInfo.createdDate = DateTimeUtils.getCurrentDateInUTC(DateTimeUtils.PATIENT_ATTR_CREATE_FORMAT)
        patientOtherInfoRepository.createPatientOtherData(otherInfo.apply {
            patientMasterAttrs = patientMasterAttributes
        })
    }) { pResult, oResult ->
        return@zip if (pResult.status == Result.State.SUCCESS && oResult.status == Result.State.SUCCESS) {
            Result.Success(pResult.data, "Patient created successfully")
        } else oResult
    }.asLiveData()

    fun updatePatient(patient: Patient, otherInfo: PatientOtherInfo) = executeLocalQuery {
        patientPersonalInfoRepository.updatePatient(patient)
    }.zip(executeLocalQuery {
        patientOtherInfoRepository.updateOrInsertPersonalAttributes(patient.uuid, otherInfo.apply {
            patientMasterAttrs = patientMasterAttributes
        })
    }) { pResult, oResult ->
        return@zip if (pResult.status == Result.State.SUCCESS && oResult.status == Result.State.SUCCESS) {
            Result.Success(pResult.data, "Patient updated successfully")
        } else oResult
    }.asLiveData()

}
