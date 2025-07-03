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
import org.intelehealth.data.offline.entity.PatientOtherInfo
import org.intelehealth.data.offline.entity.PersonAddress
import org.intelehealth.data.provider.patient.detail.PatientDetailRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 26-05-2025 - 12:16.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class PatientDetailViewModel @Inject constructor(
    private val detailRepository: PatientDetailRepository,
    regFieldRepository: RegFieldRepository,
    networkHelper: NetworkHelper
) : RegFieldViewModel(regFieldRepository, networkHelper = networkHelper) {

    private var patientPersonalDetail = MutableLiveData<Patient>()
    val patientPersonalLiveData: LiveData<Patient> get() = patientPersonalDetail

    private var patientAddressDetail = MutableLiveData<PersonAddress>()
    val patientAddressLiveData: LiveData<PersonAddress> get() = patientAddressDetail

    private var patientOtherDetail = MutableLiveData<PatientOtherInfo>()
    val patientOtherLiveData: LiveData<PatientOtherInfo> get() = patientOtherDetail

    fun fetchPatientPersonalDetail(patientId: String) = viewModelScope.launch {
        val patient = async { detailRepository.fetchPatientById(patientId) }.await()
        patientPersonalDetail.postValue(patient)
    }

    fun fetchPatientAddress(patientId: String) = viewModelScope.launch {
        val address = async { detailRepository.fetchPatientAddress(patientId) }.await()
        patientAddressDetail.postValue(address)
    }

    fun fetchPatientOtherDetails(patientId: String) = viewModelScope.launch {
        val otherInfo = async { detailRepository.fetchPatientAttributes(patientId) }.await()
        patientOtherDetail.postValue(otherInfo)
    }

    fun editPatientDetails(patient: Patient) {

    }
}
