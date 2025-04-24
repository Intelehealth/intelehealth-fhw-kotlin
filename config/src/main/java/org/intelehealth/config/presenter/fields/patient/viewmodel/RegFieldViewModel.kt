package org.intelehealth.config.presenter.fields.patient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.room.entity.PatientRegistrationFields
import org.intelehealth.config.utility.PatientInfoGroup
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 12-04-2024 - 12:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
open class RegFieldViewModel @Inject constructor(
    private val repository: RegFieldRepository
) : BaseViewModel() {

    private var personalSectionFieldsData = MutableLiveData<List<PatientRegistrationFields>>()
    val personalSectionFieldsLiveData: LiveData<List<PatientRegistrationFields>> get() = personalSectionFieldsData

    private var addressSectionFieldsData = MutableLiveData<List<PatientRegistrationFields>>()
    val addressSectionFieldsLiveData: LiveData<List<PatientRegistrationFields>> get() = addressSectionFieldsData

    private var otherSectionFieldsData = MutableLiveData<List<PatientRegistrationFields>>()
    val otherSectionFieldsLiveData: LiveData<List<PatientRegistrationFields>> get() = otherSectionFieldsData

    fun fetchEnabledPersonalRegFields() = repository.getAllEnabledGroupField(PatientInfoGroup.PERSONAL)

    fun fetchEnabledAddressRegFields() = repository.getAllEnabledGroupField(PatientInfoGroup.ADDRESS)

    fun fetchEnabledOtherRegFields() = repository.getAllEnabledGroupField(PatientInfoGroup.OTHER)

    fun fetchEnabledAllRegFields() = repository.getAllEnabledLiveFields()

    fun fetchPersonalRegFields() {
        viewModelScope.launch {
            val personalFields = repository.getGroupFields(PatientInfoGroup.PERSONAL)
            personalSectionFieldsData.postValue(personalFields)
        }
    }

    fun fetchAddressRegFields() {
        viewModelScope.launch {
            val addressFields = repository.getGroupFields(PatientInfoGroup.ADDRESS)
            addressSectionFieldsData.postValue(addressFields)
        }
    }

    fun fetchOtherRegFields() {
        viewModelScope.launch {
            val otherFields = repository.getGroupFields(PatientInfoGroup.OTHER)
            otherSectionFieldsData.postValue(otherFields)
        }
    }
}
