package org.intelehealth.config.presenter.fields.patient.viewmodel

import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.fields.patient.data.PatientVitalRepository

/**
 * Created by Lincon Pradhan R. on 24-06-2024 - 11:22.
 * Email : lincon@intelehealth.org
 * Mob   :
 **/
class PatientVitalViewModel(private val repository: PatientVitalRepository) : BaseViewModel() {

    fun getAllEnabledLiveFields() = repository.getAllEnabledLiveFields()
    suspend fun getAllEnabledFields() = repository.getAllEnabledFields()
}
