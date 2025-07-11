package org.intelehealth.data.provider.prescription

import org.intelehealth.data.offline.dao.ObservationDao
import org.intelehealth.data.offline.dao.ProviderDao
import org.intelehealth.data.provider.utils.EncounterType
import org.intelehealth.data.provider.utils.ObsConcept
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 09-07-2025 - 16:06.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PrescriptionDetailRepository @Inject constructor(
    private val providerDao: ProviderDao,
    private val observationDao: ObservationDao
) {
    fun getDoctorAge(doctorId: String) = providerDao.getProviderAge(doctorId)

    private fun getPrescriptionItemDetails(
        visitId: String,
        visitNoteEncounterConceptId: String = EncounterType.VISIT_NOTES.value,
        obsConceptId: String
    ) = observationDao.getVisitNoteItemDetails(visitId, visitNoteEncounterConceptId, obsConceptId)

    fun getPrescribedMedicationDetails(
        visitId: String
    ) = getPrescriptionItemDetails(visitId = visitId, obsConceptId = ObsConcept.JSV_MEDICATIONS.value)

    fun getMedicalAdviceDetails(
        visitId: String
    ) = getPrescriptionItemDetails(visitId = visitId, obsConceptId = ObsConcept.MEDICAL_ADVICE.value)

    fun getRequestTestDetails(
        visitId: String
    ) = getPrescriptionItemDetails(visitId = visitId, obsConceptId = ObsConcept.REQUESTED_TESTS.value)

    fun getReferredSpecialityDetails(
        visitId: String
    ) = getPrescriptionItemDetails(visitId = visitId, obsConceptId = ObsConcept.REFERRED_SPECIALIST.value)

    fun getDiagnosisDetails(
        visitId: String
    ) = getPrescriptionItemDetails(visitId = visitId, obsConceptId = ObsConcept.TELEMEDICINE_DIAGNOSIS.value)
}
