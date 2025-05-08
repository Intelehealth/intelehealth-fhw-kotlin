package org.intelehealth.app.ui.patient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel
import org.intelehealth.data.offline.entity.Patient
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
    private val patientRegFieldRepository: RegFieldRepository
) : RegFieldViewModel(patientRegFieldRepository, networkHelper = networkHelper) {

    private var patientData: MutableLiveData<Patient> = MutableLiveData()
    val patient: LiveData<Patient> get() = patientData

    /**
     * Fetches the personal information of a patient by their ID.
     *
     * @param patientId The ID of the patient whose information is to be fetched.
     * @return A LiveData object containing the patient's personal information.
     */
    fun fetchPersonalInfo(patientId: String?): LiveData<Patient> {
        viewModelScope.launch {
            val data = patientId?.let {
                async { patientPersonalInfoRepository.getPatientById(it) }.await()
            } ?: Patient().apply { uuid = UUID.randomUUID().toString() }
            patientData.postValue(data)
        }
        return patient
    }
//
//    fun fetchOtherInfo() = patientOtherInfoRepository.getPatientOtherInfo()
//
//    fun updatePersonalInfo() = patientPersonalInfoRepository.updatePatientPersonalInfo()
//
//    fun updateOtherInfo() = patientOtherInfoRepository.updatePatientOtherInfo()
}
