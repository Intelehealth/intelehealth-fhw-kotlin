package org.intelehealth.config.presenter.fields.patient.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.intelehealth.config.presenter.fields.patient.data.DiagnosticsRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.DiagnosticsViewModel

class DiagnosticsViewModelFactory (private val repository: DiagnosticsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiagnosticsViewModel(repository) as T
    }
}