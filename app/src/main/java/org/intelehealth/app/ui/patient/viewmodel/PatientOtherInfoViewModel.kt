package org.intelehealth.app.ui.patient.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel
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
)
