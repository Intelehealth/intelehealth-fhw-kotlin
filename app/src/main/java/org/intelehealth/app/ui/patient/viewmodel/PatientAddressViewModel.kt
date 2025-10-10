package org.intelehealth.app.ui.patient.viewmodel

import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel
import org.intelehealth.data.offline.entity.PersonAddress
import org.intelehealth.data.provider.patient.address.PatientAddressRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 04-05-2025 - 12:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class PatientAddressViewModel @Inject constructor(
    networkHelper: NetworkHelper,
    private val patientAddressRepository: PatientAddressRepository,
    patientRegFieldRepository: RegFieldRepository
) : RegFieldViewModel(patientRegFieldRepository, networkHelper = networkHelper) {

    fun fetchPatientAddress(patientId: String) = patientAddressRepository.getPatientAddressById(patientId)

    fun addAddress(address: PersonAddress) = executeLocalQuery {
        patientAddressRepository.addAddress(address)
    }.asLiveData()
}
