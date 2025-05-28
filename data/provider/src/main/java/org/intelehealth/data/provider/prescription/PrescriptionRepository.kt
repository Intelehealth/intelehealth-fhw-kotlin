package org.intelehealth.data.provider.prescription

import org.intelehealth.common.utility.DateTimeUtils
import org.intelehealth.data.offline.OfflineDatabase
import org.intelehealth.data.provider.utils.EncounterType
import javax.inject.Inject

class PrescriptionRepository @Inject constructor(private val database: OfflineDatabase) {
    fun getPrescriptionCount() = database.visitDao().getVisitStatusCount(
        EncounterType.VISIT_COMPLETE.value,
        EncounterType.PATIENT_EXIT_SURVEY.value
    )

    fun getRecentReceivedPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        database.visitDao().getRecentReceivedPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            EncounterType.EMERGENCY.value,
            searchQuery,
            limit,
            offset
        )

    fun getOlderReceivedPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        database.visitDao().getOlderReceivedPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            EncounterType.EMERGENCY.value,
            searchQuery,
            limit,
            offset
        )


    fun getRecentPendingPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        database.visitDao().getRecentPendingPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            searchQuery,
            limit,
            offset
        )

    fun getOlderPendingPrescriptions(searchQuery: String, limit: Int, offset: Int) =
        database.visitDao().getOlderPendingPrescriptions(
            EncounterType.VISIT_COMPLETE.value,
            EncounterType.PATIENT_EXIT_SURVEY.value,
            searchQuery,
            limit,
            offset
        )
}