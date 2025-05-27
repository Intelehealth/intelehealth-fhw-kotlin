package org.intelehealth.config.presenter.fields.vital.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.fields.vital.data.VitalRepository
import javax.inject.Inject

/**
 * Created by Lincon Pradhan R. on 24-06-2024 - 11:22.
 * Email : lincon@intelehealth.org
 * Mob   :
 **/
@HiltViewModel
class VitalViewModel @Inject constructor(
    private val repository: VitalRepository
) : BaseViewModel() {

    fun getAllEnabledLiveFields() = repository.getAllEnabledLiveFields()
    suspend fun getAllEnabledFields() = repository.getAllEnabledFields()
}
