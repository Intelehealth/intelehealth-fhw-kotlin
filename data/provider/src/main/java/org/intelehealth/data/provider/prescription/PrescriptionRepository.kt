package org.intelehealth.data.provider.prescription

import org.intelehealth.data.offline.dao.VisitDao
import org.intelehealth.data.provider.utils.EncounterType
import javax.inject.Inject

class PrescriptionRepository @Inject constructor(private val visitDao: VisitDao) {
    fun getPrescriptionCount() = visitDao.getVisitStatusCount(
        EncounterType.VISIT_COMPLETE.value,
        EncounterType.PATIENT_EXIT_SURVEY.value
    )

    fun getRecentReceivedPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        visitDao.getRecentReceivedPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            EncounterType.EMERGENCY.value,
            searchQuery,
            limit,
            offset
        )

    fun getOlderReceivedPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        visitDao.getOlderReceivedPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            EncounterType.EMERGENCY.value,
            searchQuery,
            limit,
            offset
        )


    fun getRecentPendingPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        visitDao.getRecentPendingPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            searchQuery,
            limit,
            offset
        )

    fun getOlderPendingPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        visitDao.getOlderPendingPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            searchQuery,
            limit,
            offset
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
