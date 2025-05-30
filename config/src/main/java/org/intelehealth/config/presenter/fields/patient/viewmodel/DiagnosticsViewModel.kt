package org.intelehealth.config.presenter.fields.patient.viewmodel

import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.fields.patient.data.DiagnosticsRepository

class DiagnosticsViewModel (private val repository: DiagnosticsRepository) : BaseViewModel() {

    fun getAllEnabledLiveFields() = repository.getAllEnabledLiveFields()
    suspend fun getAllEnabledFields() = repository.getAllEnabledFields()
}