package org.intelehealth.app.ui.patient.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel
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

    lateinit var patientId: String

    fun fetchPatientPersonalDetail() = detailRepository.fetchPatientById(patientId)

    fun fetchPatientAddress() = detailRepository.fetchPatientAddress(patientId)

    fun fetchPatientOtherDetails() = detailRepository.fetchPatientAttributes(patientId)
}
