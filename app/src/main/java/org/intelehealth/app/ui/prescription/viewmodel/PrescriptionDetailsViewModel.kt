package org.intelehealth.app.ui.prescription.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.intelehealth.common.helper.NetworkHelper
import org.intelehealth.common.ui.viewmodel.BaseViewModel
import org.intelehealth.data.offline.entity.Observation
import org.intelehealth.data.offline.entity.VisitDetail
import org.intelehealth.data.offline.model.Medication
import org.intelehealth.data.provider.prescription.PrescriptionDetailRepository
import javax.inject.Inject

/**
 * Created by Tanvir Hasan on 28-04-25
 * Email : mhasan@intelehealth.org
 *
 * This viewmodel is responsible to handle the prescription details screens data
 **/
@HiltViewModel
class PrescriptionDetailsViewModel @Inject constructor(
    private val prescriptionRepository: PrescriptionDetailRepository,
    networkHelper: NetworkHelper
) : BaseViewModel(networkHelper = networkHelper) {

    var visitId: String = ""

    private var visitData: MutableLiveData<VisitDetail> = MutableLiveData()
    val visitLiveDetail: LiveData<VisitDetail> get() = visitData

    private var medicationData: MutableLiveData<List<Medication>> = MutableLiveData()
    val medicationLiveData: LiveData<List<Medication>> get() = medicationData

    fun updateVisitDetails(visitDetail: VisitDetail) {
        visitData.value = visitDetail
    }

    fun getDoctorAge(doctorId: String) = prescriptionRepository.getDoctorAge(doctorId)

    fun getPrescribedMedicationDetails() = viewModelScope.launch {
        val flow = prescriptionRepository.getPrescribedMedicationDetails(visitId)
        flow.collectLatest {
            val medicationList = mutableListOf<Medication>()
            Timber.d { "Prescribed Medication => $it" }
            it.forEach { medication ->
                medication.value ?: return@forEach
                // Convert the string value to Medication object
                val medicationObj = Medication.stringToMedication(medication.value!!)
                medicationList.add(medicationObj)
            }
            Timber.d { "Medication List => $medicationList" }
            medicationData.postValue(medicationList)
        }
    }

    fun getMedicalAdviceDetails() = prescriptionRepository.getMedicalAdviceDetails(visitId).asLiveData()

    fun getRequestTestDetails() = prescriptionRepository.getRequestTestDetails(visitId).asLiveData()

    fun getReferredSpecialityDetails() = prescriptionRepository.getReferredSpecialityDetails(visitId).asLiveData()

    fun getDiagnosisDetails() = prescriptionRepository.getDiagnosisDetails(visitId).asLiveData()

    private fun addBulletPoints(inputString: String): String {
        // Remove all occurrences of "&null" (case-insensitive) and "null" (case-insensitive)
        // Remove "&null" or "null" with optional "&"
        val cleanedString = inputString.replace("(?i)& ?\\bnull\\b".toRegex(), "").trim()

        // Split the cleaned string into lines
        val lines = cleanedString.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val result = StringBuilder()

        // Add bullet point to each non-empty line
        for (line in lines) {
            if (line.trim().isNotEmpty()) {
                result.append("• ").append(line.trim()).append("\n")
            }
        }

        // Remove the last extra newline character
        if (result.isNotEmpty()) {
            result.setLength(result.length - 1)
        }

        return result.toString()
    }

    fun formatToBulletPoints(observations: List<Observation>): String {
        val result = StringBuilder()
        for (observation in observations) {
            observation.value?.let { value ->
                // Add bullet points to the value
                result.append("• ").append(value.trim()).append("\n")
//                val formattedValue = addBulletPoints(value)
//                if (formattedValue.isNotEmpty()) {
//                    result.append(formattedValue).append("\n")
//                }
            }
        }
        return result.toString().trim()
    }
}
