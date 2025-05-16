package org.intelehealth.app.ui.patient.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.config.presenter.fields.patient.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.patient.viewmodel.RegFieldViewModel
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.offline.entity.PatientOtherInfo
import org.intelehealth.data.provider.patient.otherinfo.PatientOtherDataRepository
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 13-05-2025 - 11:59.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
open class PatientViewModel @Inject constructor(
    private val otherInfoRepository: PatientOtherDataRepository,
    private val regFieldRepository: RegFieldRepository,
    networkHelper: NetworkHelper
) : RegFieldViewModel(regFieldRepository, networkHelper = networkHelper) {
    var patientMasterAttributes: List<PatientAttributeTypeMaster> = emptyList()

    private var patientOtherInfoData = MutableLiveData<PatientOtherInfo>()
    val patientOtherInfoLiveData: MutableLiveData<PatientOtherInfo> get() = patientOtherInfoData

    init {
        getPatientMasterAttributes()
    }

    private fun getPatientMasterAttributes() = viewModelScope.launch {
        async { otherInfoRepository.getPatientMasterAttributeUuids() }.await().let {
            patientMasterAttributes = it
        }
    }

    fun fetchPatientOtherInfo(patientId: String) = viewModelScope.launch {
        async { otherInfoRepository.getPatientOtherAttrs(patientId) }.await().let {
            patientOtherInfoData.postValue(it)
        }
    }

    fun fetchPatientPersonalOtherInfo(patientId: String) = otherInfoRepository.getPatientPersonalAttrs(patientId)
}
