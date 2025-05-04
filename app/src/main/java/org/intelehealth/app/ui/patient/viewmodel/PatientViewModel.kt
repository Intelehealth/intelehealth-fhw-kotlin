package org.intelehealth.app.ui.patient.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel
import org.intelehealth.data.provider.patient.otherinfo.PatientOtherDataRepository
import org.intelehealth.data.provider.patient.personal.PatientPersonalDataRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 20-04-2025 - 17:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class PatientViewModel @Inject constructor(
    networkHelper: NetworkHelper,
    private val patientPersonalInfoRepository: PatientPersonalDataRepository,
    private val patientOtherInfoRepository: PatientOtherDataRepository,
    private val patientRegFieldRepository: RegFieldRepository
) : RegFieldViewModel(patientRegFieldRepository, networkHelper = networkHelper) {

}
