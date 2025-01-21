package org.intelehealth.config.presenter.specialization.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.specialization.data.SpecializationRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 12-04-2024 - 12:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class SpecializationViewModel @Inject constructor(
    private val repository: SpecializationRepository
) : BaseViewModel() {
    fun fetchSpecialization() = repository.getAllLiveRecord()

    fun fetchSpecializationByName(name: String) = repository.getRecordByName(name)
}