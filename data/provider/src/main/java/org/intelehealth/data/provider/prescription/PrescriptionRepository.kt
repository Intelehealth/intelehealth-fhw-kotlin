package org.intelehealth.data.provider.prescription

import org.intelehealth.data.offline.dao.VisitDao
import org.intelehealth.data.offline.entity.PatientAttributeTypeMaster
import org.intelehealth.data.provider.utils.EncounterType
import org.intelehealth.data.provider.utils.ObsConcept
import org.intelehealth.data.provider.utils.VisitAttributeType
import javax.inject.Inject

class PrescriptionRepository @Inject constructor(private val visitDao: VisitDao) {

    fun getPrescriptionCount() = visitDao.getVisitStatusCount(
        EncounterType.VISIT_COMPLETE.value,
        EncounterType.PATIENT_EXIT_SURVEY.value
    )

    fun getCurrentMonthReceivedPrescriptions(searchQuery: String = "") = visitDao.getCurrentMonthReceivedPrescriptions(
        EncounterType.VISIT_COMPLETE.value,
        searchQuery
    )

    fun getReceivedPrescriptionsWithPaging(offset: Int, searchQuery: String = "") =
        visitDao.getReceivedPrescriptionsWithPaging(
            EncounterType.VISIT_COMPLETE.value,
            searchQuery,
            offset
        )

    fun getCurrentMonthPendingPrescriptions(searchQuery: String = "") = visitDao.getCurrentMonthPendingPrescriptions(
        EncounterType.VISIT_COMPLETE.value,
        EncounterType.PATIENT_EXIT_SURVEY.value,
        searchQuery
    )

    fun getPendingPrescriptionsWithPaging(offset: Int, searchQuery: String = "") =
        visitDao.getPendingPrescriptionsWithPaging(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            searchQuery,
            offset
        )
}
