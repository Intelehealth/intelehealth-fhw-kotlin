package org.intelehealth.config.presenter.fields.patient.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.intelehealth.config.presenter.fields.patient.data.PatientVitalRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.PatientVitalViewModel

/**
 * Created by Lincon Pradhan R. on 24-06-2024 - 11:22.
 * Email : lincon@intelehealth.org
 * Mob   :
 **/
class PatientVitalViewModelFactory(private val repository: PatientVitalRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PatientVitalViewModel(repository) as T
    }
}