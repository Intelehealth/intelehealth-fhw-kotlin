package org.intelehealth.app.ui.patient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.state.Result
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.data.offline.entity.Patient
import org.intelehealth.data.offline.entity.PatientOtherInfo
import org.intelehealth.data.provider.patient.otherinfo.PatientOtherDataRepository
import org.intelehealth.data.provider.patient.personal.PatientPersonalDataRepository
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
    patientRegFieldRepository: RegFieldRepository
) : PatientViewModel(
    otherInfoRepository = patientOtherInfoRepository,
    regFieldRepository = patientRegFieldRepository,
    networkHelper = networkHelper
) {

    private var patientData: MutableLiveData<Patient> = MutableLiveData()
    private val patientLiveData: LiveData<Patient> get() = patientData

    /**
     * Fetches the personal information of a patient by their ID.
     *
     * @param patientId The ID of the patient whose information is to be fetched.
     * @return A LiveData object containing the patient's personal information.
     */
    fun fetchPersonalInfo(patientId: String?): LiveData<Patient> {
        viewModelScope.launch {
            val data = patientId?.let {
                fetchPatientOtherInfo(it)
                async { patientPersonalInfoRepository.getPatientById(it) }.await()
            } ?: Patient().apply { uuid = UUID.randomUUID().toString() }
            patientData.postValue(data)
        }
        return patientLiveData
    }

    fun createPatient(patient: Patient, otherInfo: PatientOtherInfo) = executeLocalQuery {
        patientPersonalInfoRepository.insertPatient(patient)
    }.zip(executeLocalQuery {
        patientOtherInfoRepository.createPatientOtherData(otherInfo.apply {
            patientMasterAttrs = patientMasterAttributes
        })
    }) { pResult, oResult ->
        Timber.d { "Patient Res=> ${pResult.status.name}" }
        Timber.d { "Other Res=> ${oResult.status.name}" }
        if (pResult.status == Result.State.SUCCESS && oResult.status == Result.State.SUCCESS) {
            Result.Success(pResult.data, "Patient created successfully")
        } else {
            Result.Error("Failed to create patient")
        }
    }.asLiveData()

    fun updatePatient(patient: Patient, otherInfo: PatientOtherInfo) = executeLocalQuery {
        patientPersonalInfoRepository.updatePatient(patient)
    }.zip(executeLocalQuery {
        patientOtherInfoRepository.updatePatientOtherData(
            patientOtherInfoRepository.getPatientPersonalAttributes(patient.uuid, otherInfo).apply {
                Timber.d { "$this" }
            }
        )
    }) { pResult, oResult ->
        Timber.d { "Patient Res=> ${pResult.status.name}" }
        Timber.d { "Other Res=> ${oResult.status.name}" }
        Timber.d { "Patient Res=> ${pResult.message}" }
        Timber.d { "Other Res=> ${oResult.message}" }

        return@zip if (pResult.status == Result.State.SUCCESS && oResult.status == Result.State.SUCCESS) {
            Result.Success(pResult.data, "Patient updated successfully")
        } else oResult
    }.asLiveData()

}
